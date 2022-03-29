package org.micro.reading.cloud.account.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.micro.reading.book.feign.client.BookClient;
import org.micro.reading.cloud.account.dao.UserLikeSeeMapper;
import org.micro.reading.cloud.account.service.UserLikeSeeService;
import org.micro.reading.cloud.account.service.task.LikeSeeClickTask;
import org.micro.reading.cloud.common.cache.RedisAccountKey;
import org.micro.reading.cloud.common.cache.RedisExpire;
import org.micro.reading.cloud.common.cache.RedisService;
import org.micro.reading.cloud.common.pojo.account.UserLikeSee;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.micro.reading.cloud.common.vo.SimpleBookVO;
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
 * @date 2022年03月18日 15:50
 */
@Service
public class UserLikeSeeServiceImpl implements UserLikeSeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikeSeeServiceImpl.class);

    @Autowired
    private UserLikeSeeMapper userLikeSeeMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private ExecutorService commonQueueThreadPool;

    @Override
    public Result likeSeeClick(Integer userId, String bookId, Integer value) {
        try {
            // 0取消喜欢， 1增加喜欢
            if (0 == value) {
                userLikeSeeMapper.deleteByUserIdAndBookId(userId, bookId);
            } else {
                UserLikeSee like = new UserLikeSee();
                like.setUserId(userId);
                like.setBookId(bookId);
                userLikeSeeMapper.insert(like);
            }
            // 更新缓存
            LikeSeeClickTask likeSeeClickTask = new LikeSeeClickTask(redisService, bookId, value);
            commonQueueThreadPool.execute(likeSeeClickTask);
        } catch (Exception e) {
            LOGGER.error("用户喜欢点击操作异常：{}", e);
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    @Override
    public Result getBookLikesCount(String bookId) {
        Integer likeCount = redisService.getHashVal(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, Integer.class);
        if (likeCount == null) {
            likeCount = userLikeSeeMapper.findPageWithCount(bookId);
            redisService.setHashValExpire(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, likeCount, RedisExpire.HOUR);
        }
        return ResultUtil.success(likeCount);
    }

    @Override
    public Result getUserLikeBookList(Integer userId, Integer page, Integer limit) {
        try {
            PageHelper.startPage(page, limit);
            Page<UserLikeSee> pageWithResult = (Page<UserLikeSee>) userLikeSeeMapper.findPageWithResult(userId);
            List<SimpleBookVO> books = new ArrayList<>();
            for (int i = 0; i < pageWithResult.size(); i++) {
                SimpleBookVO vo = new SimpleBookVO();
                UserLikeSee likeSee = pageWithResult.get(i);
                Book book = bookClient.getBookById(likeSee.getBookId()).getData();
                if (book != null) {
                    BeanUtils.copyProperties(book, vo);
                    books.add(vo);
                }
            }
            return ResultUtil.success(books);
        } catch (Exception e) {
            LOGGER.error("获取用户[{}]喜欢书单异常：{}", userId, e);
            return ResultUtil.fail();
        }
    }

    @Override
    public Result userLikeThisBook(Integer userId, String bookId) {
        int result = 0;
        try {
            result = userLikeSeeMapper.selectCountByUserAndBookId(userId, bookId);
        } catch (Exception e) {
            LOGGER.error("查询喜欢此书异常：{}", e);
        }
        return ResultUtil.success(result);
    }
}
