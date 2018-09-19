package com.dsxx.base.util;

import com.dsxx.base.service.exception.DecodeAccessTokenException;
import com.dsxx.base.service.exception.DecodeVisitTokenException;

public class CDTToken {

    private static String TOKEN_KEY = "_a";
    private static String TOKEN_KEY_XMPP = "_axmpp";

    /**
     * 根据userid生成accesstoken；
     *
     * @param userId
     * @return
     */
    public static String encodeToken(int userId, String password) {
        String access_token = EncryUtil.encode(userId + TOKEN_KEY + "_" + password);
        return access_token;
    }

    /**
     * 根据userid生成accesstoken；
     *
     * @param userId
     * @return
     */
    public static String encodeToken2(int userId, String password, String uuId) {
        String access_token = EncryUtil.encode(userId + TOKEN_KEY + "_" + password + "_" + uuId);
        return access_token;
    }

    /**
     * 返回userid
     *
     * @param token
     * @return
     * @throws DecodeAccessTokenException
     */
    public static int decodeToken(String token) throws DecodeAccessTokenException {
        int userId = 0;
        try {
            String decodeToken = EncryUtil.decode(token);
            String userIdstr = decodeToken.split("_")[0];
            userId = Integer.valueOf(userIdstr);
        } catch (Exception e) {
            throw new DecodeAccessTokenException("decode access_token出错");
        }
        return userId;

    }


    /**
     * 返回userid
     *
     * @param token
     * @return
     * @throws DecodeAccessTokenException
     */
    public static String decodeTokenGetPassword(String token) throws DecodeAccessTokenException {
        String password = "";
        try {
            String decodeToken = EncryUtil.decode(token);
            password = decodeToken.split("_")[2];
        } catch (Exception e) {
            throw new DecodeAccessTokenException("decode access_token出错");
        }
        return password;

    }

    /**
     * 返回解析token之后的数组长度（用于判断是否含有uuId，含有uuId长度为4，不包含长度为3）
     *
     * @param token
     * @return
     * @throws DecodeAccessTokenException
     */
    public static int decodeTokenGetListLength(String token) throws DecodeAccessTokenException {
        int uuId = 0;
        try {
            String decodeToken = EncryUtil.decode(token);
            System.out.println(decodeToken);
            uuId = decodeToken.split("_").length;
        } catch (Exception e) {
            throw new DecodeAccessTokenException("decode access_token出错");
        }
        return uuId;

    }


    /**
     * 根据userid生成用于访问个人信息的token；
     *
     * @param userId
     * @return
     */
    public static String encodeVisitToken(int userId) {
        String access_token = EncryUtil.encode(TOKEN_KEY + "_" + userId);
        return access_token;
    }

    /**
     * 返回userid
     *
     * @param token
     * @return
     * @throws DecodeVisitTokenException
     */
    public static int decodeVisitToken(String token) throws DecodeVisitTokenException {
        if (token.startsWith("1000000")) {
            return Integer.valueOf(token);
        }
        int userId = 0;
        try {
            String decodeToken = EncryUtil.decode(token);
            String userIdstr = decodeToken.split("_")[2];
            userId = Integer.valueOf(userIdstr);
        } catch (Exception e) {
            throw new DecodeVisitTokenException("visit token出错");
        }
        return userId;

    }
}
