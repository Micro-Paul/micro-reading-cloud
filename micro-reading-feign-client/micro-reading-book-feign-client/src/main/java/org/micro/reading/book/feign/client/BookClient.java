package org.micro.reading.book.feign.client;

import org.micro.reading.book.feign.fallback.BookClientFallBack;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author micro-paul
 * @date 2022年03月18日 16:48
 */
@FeignClient(contextId = "book", name = "micro-reading-book", fallbackFactory = BookClientFallBack.class)
public interface BookClient {

    @RequestMapping("/book/getBookById")
    Result<Book> getBookById(@RequestParam("bookId") String bookId);
}
