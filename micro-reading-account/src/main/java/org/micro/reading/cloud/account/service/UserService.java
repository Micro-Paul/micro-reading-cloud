package org.micro.reading.cloud.account.service;

import org.micro.reading.cloud.account.bo.UserBO;
import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:42
 */
public interface UserService {


    /**
     * 注册账户
     *
     * @param userBO
     * @return org.micro.reading.cloud.common.result.Result
     * @author micro-paul
     * @date 2022/3/18 14:57
     */
    Result register(UserBO userBO);

    Result login(String loginName, String password);
}
