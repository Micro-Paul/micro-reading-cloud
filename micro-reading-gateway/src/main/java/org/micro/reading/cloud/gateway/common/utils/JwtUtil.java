package org.micro.reading.cloud.gateway.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.micro.reading.cloud.common.constant.JwtConstant;
import org.micro.reading.cloud.common.pojo.account.User;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;

/**
 * @author micro-paul
 * @date 2022年03月21日 15:15
 */
public class JwtUtil {


    public static Result validationToken(String jwt) {

        try {
            Claims claims = Jwts.parser().setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(jwt).getBody();
            User user = new User();
            user.setUuid(claims.get("uuid").toString());
            user.setLoginName(claims.get("loginName").toString());
            user.setNickName(claims.get("nickName").toString());
            if (claims.get("phoneNumber") != null) {
                user.setPhoneNumber(claims.get("phoneNumber").toString());
            }
            user.setId(Integer.parseInt(claims.get("id").toString()));
            user.setHeadImgUrl(claims.get("headImgUrl").toString());
            return ResultUtil.success(user);
        } catch (ExpiredJwtException e) {
            // 已过期令牌
            return ResultUtil.authExpired();
        } catch (SignatureException e) {
            // 伪造令牌
            return ResultUtil.unAuthorized();
        } catch (Exception e) {
            // 系统错误
            return ResultUtil.unAuthorized();
        }
    }
}
