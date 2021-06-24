package com.xzf.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author 呵呵厉害了
 * @date 2021/06/22 16:13
 **/
@Component
public class JwtUtil {
    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private Long expiration;

    /**
     * 根据userDetails生成jwttoken
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims=new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * jwt 生成
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                //设置过期时间
                .setExpiration(generateExpirationDate())
                //加密方式以及盐
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 计算过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token状态
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    /**
     * token是否可以被刷新
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * token 是否失效
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 密钥失效时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.getExpiration();
    }


    /**
     * 从用户名获取token
     *
     * @return
     */

    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;

    }

    /**
     * 根据token获取负载
     * @param token
     * @return
     */
    private Claims getClaimFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }


}
