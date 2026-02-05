package com.qyhstech.core.crypto;

import com.qyhstech.core.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.experimental.UtilityClass;

/**
 * 使用BCrypt进行密码加密
 */
@UtilityClass
public class QyBcrypt {

//    /**
//     * 加密密
//     *
//     * @param input
//     * @return
//     */
//    public String encrypt(String input) {
//        return BCrypt.hashpw(input, BCrypt.gensalt());
//    }
//
//    /**
//     * 校验密码是否正确
//     *
//     * @param input
//     * @param encryptPassword
//     * @return
//     */
//    public boolean check(String input, String encryptPassword) {
//        return BCrypt.checkpw(input, encryptPassword);
//    }


    public static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密数据
     *
     * @param source
     * @return
     */
    public static String encrypt(CharSequence source) {
        return bCryptPasswordEncoder.encode(source);
    }

    /**
     * 是否匹配密码
     *
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public static boolean check(CharSequence rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }



}
