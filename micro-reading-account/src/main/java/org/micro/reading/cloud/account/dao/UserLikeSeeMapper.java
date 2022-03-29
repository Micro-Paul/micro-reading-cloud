package org.micro.reading.cloud.account.dao;

import org.apache.ibatis.annotations.Param;
import org.micro.reading.cloud.common.pojo.account.UserLikeSee;

import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月18日 15:55
 */
public interface UserLikeSeeMapper {


    int deleteByUserIdAndBookId(@Param("userId")  Integer userId, @Param("bookId")  String bookId);

    int insert(UserLikeSee like);

    Integer findPageWithCount(String bookId);

    List<UserLikeSee> findPageWithResult(Integer userId);

    Integer selectCountByUserAndBookId(Integer userId, String bookId);
}
