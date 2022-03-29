package org.micro.reading.cloud.account.service;

import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月18日 15:50
 */
public interface UserLikeSeeService {


    Result likeSeeClick(Integer userId, String bookId, Integer value);

    Result getBookLikesCount(String bookId);

    Result getUserLikeBookList(Integer userId, Integer page, Integer limit);

    Result userLikeThisBook(Integer userId, String bookId);
}
