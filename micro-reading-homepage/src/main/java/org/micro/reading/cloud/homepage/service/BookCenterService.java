package org.micro.reading.cloud.homepage.service;

import org.micro.reading.cloud.common.pojo.book.Book;

/**
 * @author micro-paul
 * @date 2022年03月24日 17:54
 */
public interface BookCenterService {

    /**
     * 获取图书详情
     * @param bookId
     * @return
     */
    Book getBookById(String bookId);
}
