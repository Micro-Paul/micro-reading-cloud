package org.micro.reading.cloud.account.dao;

import org.micro.reading.cloud.common.pojo.account.User;

/**
 * @author micro-paul
 * @date 2022年03月18日 14:42
 */
public interface UserMapper {


    User selectByLoginName(String loginName);

    int insert(User userNew);
}
