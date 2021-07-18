package com.github.yiuman.citrus.security.jwt;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import com.github.yiuman.citrus.support.cache.InMemoryCache;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
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
public final class JwtUtils {

    private JwtUtils() {
    }

    private static final String JWT_CACHE_NAMESPACE = "JWT";

    protected static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 从缓存里边获取JWT命名空间配置，若没有则使用默认值
     */
    private static InMemoryCache<String, Object> getJwt() {
        CitrusProperties citrusProperties = SpringUtils.getBean(CitrusProperties.class, true);
        return InMemoryCache
                .get(JWT_CACHE_NAMESPACE,
                        LambdaUtils.consumerWrapper(cache -> ConvertUtils.objectToMap(citrusProperties.getJwt()).forEach(cache::save)),
                        true);
    }

    public static JwtToken generateToken(String identity, Map<String, Object> claims) {
        return generateToken(identity, null, claims);
    }

    public static JwtToken generateToken(String identity, Long expireInSeconds, Map<String, Object> claims) {
        claims = Optional.ofNullable(claims).orElse(new HashMap<>(1));
        claims.put(getIdentityKey(), identity);
        expireInSeconds = Optional.ofNullable(expireInSeconds).orElse((Long) getJwt().find(JwtProperties.JwtConstants.Attribute.VALIDATE_IN_SECONDS));
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
        String bearerToken = request.getHeader((String) getJwt().find(JwtProperties.JwtConstants.Attribute.HEADER));
        String tokenPrefix = (String) getJwt().find(JwtProperties.JwtConstants.Attribute.PREFIX);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        //如果头部没有，尝试从cookies中取
        if(bearerToken==null) {
        	Cookie[] ck = request.getCookies();
        	if(ck!=null)
        		for(Cookie c : ck) {
        			if("token".equalsIgnoreCase(c.getName()))
        				return c.getValue();
        		}
        }
        return null;
    }

    public static String getIdentityKey() {
        return (String) getJwt().find(JwtProperties.JwtConstants.Attribute.IDENTITY);
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
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode((String) getJwt().find(JwtProperties.JwtConstants.Attribute.SECRET)));
    }

}
