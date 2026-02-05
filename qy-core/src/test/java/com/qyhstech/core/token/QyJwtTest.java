package com.qyhstech.core.token;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Map;

class QyJwtTest {


    public class QyJwtUtil extends QyJwt {

        @Override
        protected String getSecretKey() {
            return "ZmQ0ZGI5NjQ0MDQwY2I4MjMxYJmNTE0MzI=";
        }
    }

    private QyJwtUtil jwtUtil = new QyJwtUtil();

    @Test
    void generateTokenFromOldToken() {
    }

    public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ZTE5NGJhODgyZjY0ZDdmYjg4MDEwOGRlYjc4NWM2MSIsInN1YiI6ImFkbWluIiwiaWF0IjoxNzE5ODg5ODA3LCJleHAiOjE3MTk4ODk4NjcsImlkIjoxfQ.cWtzpKVzEOTiILYLDa89EN_h6oUZxoPKG_MRcI3X1ic";

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken("admin");
        System.out.println(token);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("admin", Map.of("id", 1));
        System.out.println("带Claim的Token");
        System.out.println(token);
    }

    @Test
    void testGenerateToken1() {
        String token = jwtUtil.generateToken("admin", Map.of("id", 1), 60L);
        System.out.println("带Claim和过期时间的Token");
        System.out.println(token);
    }

    @Test
    void isTokenValid() {
        System.out.println(jwtUtil.isTokenValid(token));
    }

    @Test
    void testIsTokenValid() {
        System.out.println(jwtUtil.isTokenValid(token, "admin"));
        System.out.println(jwtUtil.isTokenValid(token, "admi"));
    }

    @Test
    void isTokenExpired() {
        String token = jwtUtil.generateToken("admin", Map.of("id", 1), 30L);

        while (true) {
            boolean hasExpired = jwtUtil.isTokenExpired(token);
            System.out.println("JwtTOken当前时间：" + LocalDateTime.now() + ",是否过期：" + hasExpired);
            if (hasExpired) {
                break;
            }
            ThreadUtil.sleep(1000);
        }
    }

    @Test
    void testIsTokenExpired() {
        System.out.println(jwtUtil.isTokenExpired(token, "admin"));
    }

    @Test
    void getExpiration() {
        System.out.println(jwtUtil.getExpiration(token));
    }

    @Test
    void getAccount() {
        System.out.println(jwtUtil.getAccount(token));
    }

    @Test
    void getIssuedAt() {
        System.out.println(jwtUtil.getIssuedAt(token));
    }

    @Test
    void getClaim() {
        System.out.println(jwtUtil.getClaim(token, "account"));
    }

    @Test
    void testGetClaim() {
    }

    @Test
    void testGetClaim1() {
    }

    @Test
    void parseToken() {
        System.out.println(jwtUtil.parseToken(token));
    }

    @Test
    void parseHeader() {
        System.out.println(jwtUtil.parseHeader(token));
    }

    @Test
    void parseClaim() {
        System.out.println(jwtUtil.parseClaim(token));
    }

    @Test
    void parseTokenValid() {
        System.out.println(jwtUtil.parseTokenValid(token));
    }

    @Test
    void getKey() {
        SecretKey secretKey = jwtUtil.genSecretKey();
        System.out.println(jwtUtil.getKeyStr(secretKey));
    }

    @Test
    void genSecretKey() {
        SecretKey secretKey = jwtUtil.genSecretKey("haha");
        System.out.println(jwtUtil.getKeyStr(secretKey));
    }
}