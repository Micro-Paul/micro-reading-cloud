package org.micro.reading.cloud.account.service;

import org.micro.reading.cloud.account.bo.UserBookshelfBO;
import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月22日 9:43
 */
public interface UserBookshelfService {

    Result syncUserBookshelf(Integer userId, UserBookshelfBO userBookshelfBO);

    Result getUserBookshelf(Integer userId);

    Result userBookshelfExistBook(Integer userId, String bookId);
}
