package org.micro.reading.cloud.account.service.impl;

import org.micro.reading.book.feign.client.BookClient;
import org.micro.reading.cloud.account.bo.UserBookshelfBO;
import org.micro.reading.cloud.account.dao.UserBookshelfMapper;
import org.micro.reading.cloud.account.service.UserBookshelfService;
import org.micro.reading.cloud.account.service.task.UserBookshelfTask;
import org.micro.reading.cloud.account.vo.UserBookshelfVO;
import org.micro.reading.cloud.common.pojo.account.UserBookshelf;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author micro-paul
 * @date 2022年03月22日 9:43
 */
@Service
public class UserBookshelfServiceImpl implements UserBookshelfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookshelfServiceImpl.class);

    @Autowired
    private UserBookshelfMapper userBookshelfMapper;

    /**
     * 书架同步任务线程池
     */
    @Autowired
    private ExecutorService userBookshelfQueueThreadPool;

    /**
     * 图书资源中心feign接口
     */
    @Autowired
    private BookClient bookClient;

    @Override
    public Result syncUserBookshelf(Integer userId, UserBookshelfBO userBookshelfBO) {
        UserBookshelf userBookshelf = new UserBookshelf();
        BeanUtils.copyProperties(userBookshelfBO, userBookshelf);
        userBookshelf.setLastReadTime(System.currentTimeMillis());
        // 异步处理同步任务
        UserBookshelfTask userBookshelfTask = new UserBookshelfTask(userBookshelfBO.getSyncType(), userBookshelf, userBookshelfMapper, userId);
        userBookshelfQueueThreadPool.execute(userBookshelfTask);
        return ResultUtil.success();
    }

    @Override
    public Result getUserBookshelf(Integer userId) {
        List<UserBookshelf> pageWithResult = userBookshelfMapper.findPageWithResult(userId);
        List<UserBookshelfVO> bookshelfs = new ArrayList<>();
        for (int i = 0; i < pageWithResult.size(); i++) {
            UserBookshelf bookshelf = pageWithResult.get(i);
            Book book = bookClient.getBookById(bookshelf.getBookId()).getData();
            if (book != null) {
                UserBookshelfVO vo = new UserBookshelfVO();
                BeanUtils.copyProperties(bookshelf, vo);
                vo.setBookName(book.getBookName());
                vo.setAuthorName(book.getAuthorName());
                vo.setImgUrl(book.getImgUrl());
                bookshelfs.add(vo);
            }
        }
        return ResultUtil.success(bookshelfs);
    }

    @Override
    public Result userBookshelfExistBook(Integer userId, String bookId) {
        int result = 0;
        try {
            result = userBookshelfMapper.selectCountByUserAndBookId(userId, bookId);
        } catch (Exception ex) {
            LOGGER.error("查询图书是否在用户书架里异常：{}", ex);
        }
        return ResultUtil.success(result);
    }
}
