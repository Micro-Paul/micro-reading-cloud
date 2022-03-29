package org.micro.reading.cloud.book.dao;

import org.micro.reading.cloud.common.pojo.book.Book;

/**
 * @author micro-paul
 * @date 2022年03月16日 17:20
 */
public interface BookMapper {


    // Book selectById(Integer id);

    Book selectByBookId(String bookId);
}
