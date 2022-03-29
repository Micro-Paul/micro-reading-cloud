package org.micro.reading.cloud.book.service;

import org.micro.reading.cloud.book.vo.BookVO;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月16日 16:59
 */
public interface BookService {

    /**
     * 查询图书基本信息
     *
     * @param bookId
     * @return org.micro.reading.cloud.common.result.Result
     * @author micro-paul
     * @date 2022/3/16 17:00
     */
    Result<Book> getBookById(String bookId);

    /**
     * 获取图书详情
     *
     * @param bookId
     * @return org.micro.reading.cloud.common.result.Result<BookVO>
     * @author micro-paul
     * @date 2022/3/16 17:00
     */
    Result<BookVO> getBookDetails(String bookId);
}
