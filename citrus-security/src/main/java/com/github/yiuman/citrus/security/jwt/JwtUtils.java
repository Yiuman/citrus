package com.github.yiuman.citrus.security.jwt;

import com.github.yiuman.citrus.support.cache.InMemoryCache;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JWT工具
 * @author yiuman
 * @date 2020/4/6
 */
public final class JwtUtils {

    private JwtUtils() {
    }

    private static final String JWT_CACHE_NAMESPACE = "JWT";

    protected static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 从缓存里边获取JWT命名空间配置，若没有则使用默认值
     */
    private static InMemoryCache<String, Object> jwt = InMemoryCache
            .get(JWT_CACHE_NAMESPACE,
                    LambdaUtils.consumerWrapper(cache -> ConvertUtils.objectToMap(new JwtProperties()).forEach(cache::save)),
                    true);

    public static String generateToken(String identity, Map<String, Object> claims) {
        return generateToken(identity, null, claims);
    }

    public static String generateToken(String identity, Long expireInSeconds, Map<String, Object> claims) {
        claims = Optional.ofNullable(claims).orElse(Maps.newHashMap());
        claims.put(getIdentityKey(), identity);
        expireInSeconds = Optional.ofNullable(expireInSeconds).orElse((Long) jwt.find("tokenValidateInSeconds"));
        return Jwts.builder()
                .setSubject(identity)
                .setClaims(claims)
                .signWith(signKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + expireInSeconds * 1000))
                .compact();
    }

    public static boolean validateToken(String token) {
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

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader((String) jwt.find("tokenHeader"));
        String tokenPrefix = (String) jwt.find("tokenPrefix");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        return null;
    }

    public static String getIdentityKey() {
        return (String) jwt.find("identityKey");
    }

    public static Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

    protected static Key signKey() {
        //对Secret进行Base64编码
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode((String) jwt.find("secret")));
    }


}
