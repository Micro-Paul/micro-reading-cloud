package org.micro.reading.cloud.account.dao;

import org.micro.reading.cloud.common.pojo.account.UserBookshelf;

import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月22日 9:48
 */
public interface UserBookshelfMapper {


    Integer insert(UserBookshelf bookshelf);

    Integer updateByUserIdAndBookId(UserBookshelf bookshelf);

    Integer deleteById(Integer id);

    List<UserBookshelf> findPageWithResult(Integer userId);

    Integer selectCountByUserAndBookId(Integer userId, String bookId);
}
