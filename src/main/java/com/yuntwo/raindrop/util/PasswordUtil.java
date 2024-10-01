package com.yuntwo.raindrop.util;

import com.yuntwo.raindrop.entity.Users;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 用户密码校验
 */
public class PasswordUtil {
    /**
     * 判断密码是否相等
     * @param user
     * @param newPassword
     * @return
     */
    public static boolean matches(Users user, String newPassword) {
        return user.getPassword().equals(encryptPassword(newPassword, user.getSalt()));
    }

    /**
     * 获取加密后的密码
     * @param password
     * @param salt
     * @return 将哈希值转化为十六进制字符串
     */
    public static String encryptPassword(String password, String salt) {
        return new Md5Hash(salt + password + salt).toHex();
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt()
    {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(3).toHex();
    }

    public static void main(String[] args) {
        String salt = randomSalt();
        System.out.println(salt);
        System.out.println(encryptPassword("123456",salt));

    }

}
