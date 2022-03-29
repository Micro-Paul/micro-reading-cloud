package org.micro.reading.cloud.account.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:51
 */
@ApiModel(value = "用户注册信息")
@Data
public class UserBO {

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名(至少4个字符)", example = "micro")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", example = "123321")
    private String userPwd;

    /**
     * 中文名
     */
    @ApiModelProperty(value = "昵称", example = "杨过")
    private String nickName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "手机", example = "13000000000")
    private String phoneNumber;

}
