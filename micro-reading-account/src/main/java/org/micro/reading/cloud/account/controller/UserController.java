package org.micro.reading.cloud.account.controller;

import io.swagger.annotations.*;
import org.micro.reading.cloud.account.bo.UserBO;
import org.micro.reading.cloud.account.service.UserService;
import org.micro.reading.cloud.account.vo.AuthVO;
import org.micro.reading.cloud.common.request.RequestParams;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:40
 */
@Api(description = "用户服务接口")
@RestController
@RequestMapping("account/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Result.class)})
    @PostMapping("/register")
    public Result register(@RequestBody UserBO userBO) {
        return userService.register(userBO);
    }

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "loginName", value = "登录名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "登录密码", required = true, dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = AuthVO.class)})
    @PostMapping("/login")
    public Result login(@RequestBody RequestParams params) {
        String loginName = params.getStringValue("loginName");
        String password = params.getStringValue("password");
        return userService.login(loginName, password);
    }


}
