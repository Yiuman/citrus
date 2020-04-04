package com.github.yiuman.citrus.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JWT提供者抽象基类
 *
 * @author yiuman
 * @date 2020/3/22
 */
public abstract class AbstractJwtProvider implements JwtProvider {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final JwtProperties jwtProperties;

    public AbstractJwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateToken(Authentication authentication, Map<String, Object> claims) {
        claims = Optional.ofNullable(claims).orElse(new HashMap<>());
        claims.put(getIdentityKey(), authentication.getCredentials());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .signWith(signKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenValidateInSeconds()))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signKey())
                    .build()
                    .parse(token);
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

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getTokenHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }

    @Override
    public String getIdentityKey() {
        return jwtProperties.getIdentityKey();
    }

    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

    protected Key signKey() {
        //对Secret进行Base64编码
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }


    /**
     * 默认实现，直接继承基类
     */
    public static class DefaultJwtProvider extends AbstractJwtProvider {

        public DefaultJwtProvider(JwtProperties jwtProperties) {
            super(jwtProperties);
        }
    }


}
