package com.qyhstech.core.token;

import org.junit.jupiter.api.Test;

public class JwtTokenHelperTest {

    @Test
    public void mainTest() {

//        JwtProperties jwtProperties = new JwtProperties();
//        jwtProperties.setSecret("haaha");
//        jwtProperties.setAppName("我的APP");
////        jwtProperties.setPcExpires(1000L);
////        jwtProperties.setAppExpires(1000L);
//
//        QyToken qyJwt = new QyToken(jwtProperties);
//
//        JwtUserInfo shiroInfo = new JwtUserInfo();
//        shiroInfo.setUserId("18515490065");
//        shiroInfo.setExpires(30L); // 设置TOken过期时间
//
//        // Token生成
//        String token = qyJwt.generateToken(shiroInfo);
//        System.out.println("Token为：" + token);
//        System.out.println("Token中账户Account信息为：" + qyJwt.getSubject(token));
//        System.out.println("Token中audience为：" + qyJwt.getAudience(token));
//        System.out.println("Token中[过期时间]为：" + qyJwt.getExpiration(token));
//        System.out.println("Token中[是否过期]为：" + qyJwt.isTokenExpired(token));
//
//        QyThreadUtil.safeSleep(3000);
//
//        System.out.println("=============");
//        System.out.println("Token中[过期时间]为：" + qyJwt.getExpiration(token));
//        System.out.println("Token中[是否过期]为：" + qyJwt.isTokenExpired(token));
//
//        // Token校验
//        System.out.println("=============");
//        boolean verify2 = qyJwt.validateToken(token, shiroInfo);
//        System.out.println("Token验证账号密码结果为：" + (verify2 ? "Yes" : "No"));
//        System.out.println("进行一次重复验证");

        //Token刷新
    }

    @Test
    public void refreshToken() {
    }

    @Test
    public void generateToken() {
    }

    @Test
    public void validateToken() {
    }

    @Test
    public void parseJwt() {
    }

    @Test
    public void getAudienceFromToken() {
    }

    @Test
    public void getSubjectFromToken() {
    }

    @Test
    public void getIssuedAtDateFromToken() {
    }

    @Test
    public void checkJwtTime() {
    }

    @Test
    public void getExpiredIn() {
    }

    @Test
    public void getExpirationDateFromToken() {
    }

    @Test
    public void isTokenExpired() {
    }

    @Test
    public void getToken() {
    }
}
