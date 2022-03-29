package org.micro.reading.cloud.account.vo;

import lombok.Data;

/**
 * @author micro-paul
 * @date 2022年03月18日 15:14
 */
@Data
public class AuthVO {
    private String token;
    private UserVO user;
}
