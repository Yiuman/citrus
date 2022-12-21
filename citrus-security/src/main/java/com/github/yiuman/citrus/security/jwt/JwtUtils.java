package com.github.yiuman.citrus.security.jwt;

import com.github.yiuman.citrus.support.utils.SpringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JWT工具
 *
 * @author yiuman
 * @date 2020/4/6
 */
@Slf4j
public final class JwtUtils {

    private static final JwtProperties JWT_PROPERTIES = SpringUtils.getBean(JwtProperties.class, true);

    private JwtUtils() {
    }

    public static JwtToken generateToken(String identity, Map<String, Object> claims) {
        return generateToken(identity, null, claims);
    }

    public static JwtToken generateToken(String identity, Long expireInSeconds, Map<String, Object> claims) {
        claims = Optional.ofNullable(claims).orElse(new HashMap<>(1));
        claims.put(getIdentityKey(), identity);
        expireInSeconds = Optional.ofNullable(expireInSeconds).orElse(JWT_PROPERTIES.getTokenValidateInSeconds());
        long expireTimestamp = System.currentTimeMillis() + expireInSeconds * 1000;
        String token = Jwts.builder()
                .setSubject(identity)
                .setClaims(claims)
                .signWith(signKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date(expireTimestamp))
                .compact();
        return new JwtToken(token, expireTimestamp);
    }

    public static boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }

    public static String resolveToken(HttpServletRequest request) {
        //获取token，若请求头中没有则从请求参数中取
        String bearerToken = Optional
                .ofNullable(request.getHeader(JWT_PROPERTIES.getTokenHeader()))
                .orElse(request.getParameter(JWT_PROPERTIES.getTokenParamName()));
        String tokenPrefix = JWT_PROPERTIES.getTokenPrefix();
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        return null;
    }

    public static String getIdentityKey() {
        return JWT_PROPERTIES.getIdentityKey();
    }

    public static Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

    public static String getIdentityClaimsValue(String token) {
        return (String) getClaims(token).get(getIdentityKey());
    }

    public static Key signKey() {
        //对Secret进行Base64编码
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_PROPERTIES.getSecret()));
    }

}
