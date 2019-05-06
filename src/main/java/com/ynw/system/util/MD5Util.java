package com.ynw.system.util;

import com.ynw.system.entity.User;
import sun.misc.BASE64Encoder;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;

public class MD5Util {

    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    /**
     *  密码加密
     * @param user
     */
    public static void encryptPassword(User user) {
        //String salt=randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getMsUserId()), hashIterations).toHex();
        //String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
        user.setPassword(newPassword);

    }

    public static String EncoderByMd5(String str) throws Exception {

           MessageDigest md5=MessageDigest.getInstance("MD5");
           BASE64Encoder base64en = new BASE64Encoder();
           String encode=base64en.encode(md5.digest(str.getBytes()));
           return encode;
}
}
