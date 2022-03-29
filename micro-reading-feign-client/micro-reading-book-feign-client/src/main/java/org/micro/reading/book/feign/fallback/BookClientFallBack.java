package org.micro.reading.book.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.micro.reading.book.feign.client.BookClient;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 图书客户端feign接口快速失败
 *
 * @author micro-paul
 * @date 2022年03月18日 16:50
 */
@Component
public class BookClientFallBack implements FallbackFactory<BookClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookClientFallBack.class);

    @Override
    public BookClient create(Throwable throwable) {

        return new BookClient() {
            @Override
            public Result<Book> getBookById(String bookId) {
                LOGGER.error("获取图书[{}]失败：{}", bookId, throwable.getMessage());
                return ResultUtil.success(null);
            }
        };
    }
}
