package org.micro.reading.cloud.book.service.impl;

import org.micro.reading.cloud.book.dao.BookMapper;
import org.micro.reading.cloud.book.service.BookService;
import org.micro.reading.cloud.book.vo.BookVO;
import org.micro.reading.cloud.common.cache.RedisBookKey;
import org.micro.reading.cloud.common.cache.RedisExpire;
import org.micro.reading.cloud.common.cache.RedisService;
import org.micro.reading.cloud.common.constant.CategoryConstant;
import org.micro.reading.cloud.common.enums.BookSerialStatusEnum;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月16日 17:02
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public Result<Book> getBookById(String bookId) {
        String bookKey = RedisBookKey.getBookKey(bookId);
        Book book = redisService.getCache(bookKey, Book.class);
        if (Objects.isNull(book)) {
            book = bookMapper.selectByBookId(bookId);
            if (Objects.nonNull(book)) {
                redisService.setCacheExpire(bookKey, book, RedisExpire.HOUR);
            }
        }
        return ResultUtil.success(book);
    }

    @Override
    public Result<BookVO> getBookDetails(String bookId) {
        Book book = getBookById(bookId).getData();
        if (Objects.isNull(book)) {
            return ResultUtil.notFound().buildMessage("找不到"+bookId+"这本书哦！");
        }
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        // 分类
        String categoryName = CategoryConstant.categorys.get(book.getDicCategory());
        vo.setCategoryName(categoryName);
        // 连载状态
        String serialStatusName = BookSerialStatusEnum.values()[book.getDicSerialStatus() - 1].getName();
        vo.setSerialStatusName(serialStatusName);
        return ResultUtil.success(vo);
    }
}
