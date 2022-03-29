package org.micro.reading.cloud.account.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.micro.reading.cloud.account.vo.UserVO;
import org.micro.reading.cloud.common.constant.JwtConstant;

import java.util.Date;

/**
 * WT工具
 *
 * @author micro-paul
 * @date 2022年03月18日 14:28
 */
public class JwtUtil {


    public static String buildJwt(Date expire, UserVO user) {

        return Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, JwtConstant.SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                .claim("loginName", user.getLoginName())
                .claim("nickName", user.getNickName())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("headImgUrl", user.getHeadImgUrl())
                .claim("uuid", user.getUuid())
                .claim("id", user.getId())
                .compact();
    }

}
