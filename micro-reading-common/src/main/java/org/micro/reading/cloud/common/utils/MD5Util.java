package org.micro.reading.cloud.common.utils;

import java.security.MessageDigest;

/**
 * @author micro-paul
 * @date 2022年03月15日 17:58
 */
public class MD5Util {

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * @param origin
     * @param charsetName
     * @return java.lang.String
     * @author micro-paul
     * @date 2022/3/15 18:01
     */
    public static String MD5Encode(String origin, String charsetName) {

        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetName || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultString;

    }

    /**
     *
     * @author micro-paul
     * @date 2022/3/16 9:20
     * @param digest
     * @return java.lang.String
     */
    private static String byteArrayToHexString(byte[] digest) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {

            sb.append(byteToHexString(digest[i]));
        }
        return sb.toString();
    }

    /**
     *
     * @author micro-paul
     * @date 2022/3/16 9:27
     * @param b
     * @return java.lang.String
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

}
