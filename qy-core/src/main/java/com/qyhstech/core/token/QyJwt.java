package com.qyhstech.core.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.json.QyJackson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Jwt工具类
 */
//@UtilityClass
@Slf4j
public abstract class QyJwt {

    protected abstract String getSecretKey();

    /**
     * 刷新Token值
     *
     * @param token 原Token
     * @return
     */
    public String generateTokenFromOldToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = parseToken(token); // 解析出用户信息
            refreshedToken = generateToken(claims.getSubject(), claims, 0L); // 重新生成一份刷新Token
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 生成Token无额外信息
     */
    public String generateToken(String subject) {
        return generateToken(subject, new HashMap<>(0), null);
    }

    /**
     * 生成Token,有额外信息
     *
     * @param subject 用户信息
     * @param claims  额外的数据
     * @return String
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, 0L);
    }

    /**
     * 构建Token方法
     *
     * @param subject     用户信息
     * @param extraClaims 额外信息
     * @param expiration  失效时间,单位秒
     * @return String
     */
    public String generateToken(String subject, Map<String, Object> extraClaims, Long expiration) {
        JwtBuilder jwt = Jwts.builder().id(IdUtil.simpleUUID()) // 确保生成不同的Token
                .header().add("typ", "JWT").add("alg", "HS256").and()
                .subject(subject) // sub(subject)：jwt所面向的用户，放登录的用户名，一个json格式的字符串，可存放userid，roldid之类，作为用户的唯一标志
                //                .claim("account", account)
                .issuedAt(new Date(System.currentTimeMillis())) //设置发布时间
                .signWith(getKey(), Jwts.SIG.HS256) //设置摘要算法
                ;
        if (Objects.nonNull(expiration) && expiration > 0) {
            jwt.expiration(Date.from(Instant.now().plusSeconds(expiration))); //设置过期时间
        }

        // 设置额外的Claim
        if (MapUtil.isNotEmpty(extraClaims)) {
            jwt.claims(extraClaims);
        }

        return jwt.compact();
    }

    /**
     * 不为空且没过期
     *
     * @param token
     * @return
     */
    public Boolean isTokenValid(String token) {
        return StrUtil.isNotBlank(token) && parseTokenValid(token);
    }

    /**
     * 验证Token是否有效 验效用户信息是否合法
     *
     * @param token   待校验的Token
     * @param subject 对比的用户信息
     * @return
     */
    public Boolean isTokenValid(String token, String subject) {
        if (StrUtil.isBlank(token)) {
            return false;
        }
        final String account = getAccount(token);
        return account != null && account.equals(subject) && parseTokenValid(token);
    }

    /**
     * 判断token失效时间是否到了
     * True 已经过期
     * False 没有过期
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        if (StrUtil.isBlank(token)) {
            return true;
        }
        final Date expiration = getExpiration(token);
        return null == expiration || expiration.before(new Date());
    }

    /**
     * 判断token失效时间是否到了
     *
     * @param token
     * @param subject
     * @return
     */
    public Boolean isTokenExpired(String token, String subject) {
        if (StrUtil.isBlank(token)) {
            return true;
        }
        final String account = getAccount(token);
        return !(StrUtil.isNotBlank(account) && account.equalsIgnoreCase(subject) && !isTokenExpired(token));
    }

    /**
     * 从Token中获取失效时间
     */
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * 取出Subject对象，可以是手机、账号、用户ID等都可以
     *
     * @param token Token
     * @return String
     */
    public String getAccount(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public Date getIssuedAt(String token) {
        return getClaim(token, Claims::getIssuedAt);
    }

    /**
     * 读取Claim
     *
     * @param token
     * @param claimKey
     * @return
     */
    public String getClaim(String token, String claimKey) {
        final Claims claims = parseToken(token);
        if (Objects.nonNull(claims)) {
            return claims.get(claimKey).toString();
        }
        return null;
    }

    /**
     * 解析Claim到指定的实体
     *
     * @param token    Token
     * @param claimKey Claim里面的Key
     * @param clz      解析到的实体类
     * @param <T>
     * @return
     */
    public <T> T getClaim(String token, String claimKey, Class<T> clz) {
        final Claims claims = parseToken(token);
        if (Objects.nonNull(claims) && Objects.nonNull(claims.get(claimKey))) {
            return QyJackson.parseObject(claims.get(claimKey).toString(), clz);
        }
        return null;
    }

    /**
     * 从Token中回去数据,根据传入不同的Function返回不同的数据
     * eg: String extractUsername(String token)
     *
     * @param token          Token
     * @param claimsResolver 要取值的字段
     * @param <T>
     * @return
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        if (Objects.nonNull(claims)) {
            return claimsResolver.apply(claims);
        }
        return null;
    }

    /**
     * 获取指定Key的Claim对象
     *
     * @param token
     * @return
     */
    public Claims parseToken(String token) {
        Jws<Claims> claims = parseClaim(token);
        if (Objects.nonNull(claims)) {
            return claims.getPayload();
        }
        return null;
    }

    /**
     * 提取Header中的值
     *
     * @param token
     * @return
     */
    public JwsHeader parseHeader(String token) {
        Jws<Claims> claims = parseClaim(token);
        if (Objects.nonNull(claims)) {
            return claims.getHeader();
        }
        return null;
    }

    /**
     * 私钥 / 生成签名的时候使用的秘钥secret，
     * 在任何场景都不应该流露出去 应该大于等于 256位(长度32及以上的字符串)，并且是随机的字符串
     *
     * @param token Token内容
     * @return
     */
    public Jws<Claims> parseClaim(String token) {
        try {
            if (StrUtil.isNotBlank(token)) {
                return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            }
        } catch (ExpiredJwtException ex) {
            log.error("parseClaim解析Token失败,Token:{},已经过期,{}", token, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("parseClaim解析Token失败", ex);
            throw ex;
        }
        return null;
    }

    /**
     * 检查Token是否有效
     *
     * @param token
     * @return
     */
    public boolean parseTokenValid(String token) {
        if (StrUtil.isBlank(token)) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error("parseTokenValid解析Token失败", ex);
            return false;
        }
    }

    /**
     * 动态设置指定不同设备的Token过期时间
     *
     * @param expires 过期时间，单位秒
     * @return
     */
    private Date generateExpirationDate(Long expires) {
        //        return new Date(Instant.now().toEpochMilli() + expires);
        return new Date(new Date().getTime() + expires * 1000);
    }

    /**
     * 生成密钥
     *
     * @return
     */
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(this.getSecretKey().getBytes());
    }

    /**
     * 使用自带的HS256生成Secretkey
     *
     * @return
     */
    public SecretKey genSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    /**
     * 由字符串生成加密key
     *
     * @param seed
     * @return
     */
    public SecretKey genSecretKey(String seed) {
        // 本地的密码解码
        byte[] encodedKey = Base64.decode(seed);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 获取加密Key的Base64格式
     *
     * @return
     */
    public String getKeyStr(SecretKey secretKey) {
        return Base64.encode(secretKey.getEncoded());
    }


}
