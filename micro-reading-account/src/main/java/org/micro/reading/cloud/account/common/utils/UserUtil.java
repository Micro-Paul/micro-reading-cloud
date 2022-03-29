package org.micro.reading.cloud.account.common.utils;

import org.micro.reading.cloud.common.utils.MD5Util;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:36
 */
public class UserUtil {

    /**
     * 获取用户盐值，用于加密用户密码
     * @author micro-paul
     * @date 2022/3/18 14:36
     * @param loginName 
     * @return java.lang.String
     */
    public static String getUserSalt(String loginName) {
        // 盐值
        String[] salts = {"sun","moon","star","sky","cloud","fog","rain","wind","rainbow"};
        int hashCode = loginName.hashCode() + 159;
        int mod = Math.abs( hashCode % 9 );
        return salts[mod];
    }

    public static String getUserEncryptPassword(String loginName, String password) {
        String pwdAndSalt = password + getUserSalt(loginName);
        return MD5Util.MD5Encode(password, "utf-8");
    }
}
