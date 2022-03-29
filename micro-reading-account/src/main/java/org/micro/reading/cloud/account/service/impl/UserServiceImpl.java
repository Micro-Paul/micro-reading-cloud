package org.micro.reading.cloud.account.service.impl;

import org.micro.reading.cloud.account.bo.UserBO;
import org.micro.reading.cloud.account.common.utils.JwtUtil;
import org.micro.reading.cloud.account.common.utils.UserUtil;
import org.micro.reading.cloud.account.dao.UserMapper;
import org.micro.reading.cloud.account.service.UserService;
import org.micro.reading.cloud.account.vo.AuthVO;
import org.micro.reading.cloud.account.vo.UserVO;
import org.micro.reading.cloud.common.constant.JwtConstant;
import org.micro.reading.cloud.common.pojo.account.User;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.micro.reading.cloud.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:42
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;


    @Override
    public Result register(UserBO userBO) {
        User user = userMapper.selectByLoginName(userBO.getLoginName());
        if (Objects.nonNull(user)) {
            return ResultUtil.verificationFailed().buildMessage(userBO.getLoginName() + "已存在，请使用其它登录名进行注册！");
        }
        User userNew = new User();
        BeanUtils.copyProperties(userBO, userNew);
        String encryptPwd = UserUtil.getUserEncryptPassword(userBO.getLoginName(), userBO.getUserPwd());
        userNew.setUserPwd(encryptPwd);
        try {
            userNew.setHeadImgUrl("https://avatars.githubusercontent.com/u/44263882?s=40&v=4");
            userNew.setUuid(CommonUtil.getUUID());
            userMapper.insert(userNew);
        } catch (Exception e) {
            LOGGER.error("注册用户失败了！{}; user:{}", e, user);
            return ResultUtil.fail().buildMessage("注册失败，服务器被吃了! ＝(#>д<)ﾉ");
        }
        return ResultUtil.success().buildMessage("注册成功！请登录吧");
    }

    @Override
    public Result login(String loginName, String password) {

        try {
            User user = userMapper.selectByLoginName(loginName);
            if (Objects.isNull(user)) {
                return ResultUtil.notFound().buildMessage("登录失败，用户不存在！");
            }
            // 校验用户密码
            String encryptPassword = UserUtil.getUserEncryptPassword(loginName, password);
            if (!encryptPassword.equals(user.getUserPwd())) {
                return ResultUtil.verificationFailed().buildMessage("登录失败，密码输入错误！");
            }
            // 登录成功，返回用户信息
            AuthVO vo = new AuthVO();
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            vo.setToken(JwtUtil.buildJwt(getLoginExpre(), userVo));
            vo.setUser(userVo);
            return ResultUtil.success(vo);

        } catch (Exception e) {
            LOGGER.error("登录失败了！{}; loginName:{}", e, loginName);
            return ResultUtil.fail().buildMessage("登录失败，服务器被吃了＝(#>д<)ﾉ ！请重试。 ");
        }
    }

    private Date getLoginExpre() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, JwtConstant.EXPIRE_DAY);
        return calendar.getTime();
    }
}
