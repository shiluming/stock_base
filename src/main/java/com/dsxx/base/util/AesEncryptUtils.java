package com.dsxx.base.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES工具类
 *
 * @author slm
 * @date 2018/06/19
 */
public class AesEncryptUtils {

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 将内容加密为byte数组
     *
     * @param content    内容
     * @param encryptKey 加密密钥
     * @return byte[] 密文内容
     * @throws Exception
     */
    public static byte[] encryptToBytes(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * 将内容加密为Base64字符串
     *
     * @param content    内容
     * @param encryptKey 加密密钥
     * @return String 密文内容
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        return Base64.encodeBase64String(encryptToBytes(content, encryptKey));
    }

    /**
     * 将内容加密为Url安全的Base64字符串，字符+和/分别变成-和_
     *
     * @param content    内容
     * @param encryptKey 加密密钥
     * @return String 密文内容
     * @throws Exception
     */
    public static String encryptToUrlSafeBase64(String content, String encryptKey) throws Exception {
        return Base64.encodeBase64URLSafeString(encryptToBytes(content, encryptKey));
    }

    /**
     * 将byte数组类型的密文解密
     *
     * @param encryptBytes 密文内容
     * @param decryptKey   解密密钥
     * @return String 明文内容
     * @throws Exception
     */
    public static String decryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 将Base64类型的密文解密
     *
     * @param encryptStr 密文内容
     * @param decryptKey 解密密钥
     * @return String 明文内容
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        return decryptByBytes(Base64.decodeBase64(encryptStr), decryptKey);
    }

}