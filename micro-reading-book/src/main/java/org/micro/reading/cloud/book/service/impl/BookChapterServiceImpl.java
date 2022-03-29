package org.micro.reading.cloud.book.service.impl;

import org.micro.reading.cloud.book.dao.BookChapterMapper;
import org.micro.reading.cloud.book.domain.BookPreviousAndNextChapterNode;
import org.micro.reading.cloud.book.service.BookChapterService;
import org.micro.reading.cloud.book.service.BookService;
import org.micro.reading.cloud.book.vo.BookChapterListVO;
import org.micro.reading.cloud.book.vo.BookChapterReadVO;
import org.micro.reading.cloud.book.vo.BookChapterVO;
import org.micro.reading.cloud.common.cache.RedisBookKey;
import org.micro.reading.cloud.common.cache.RedisExpire;
import org.micro.reading.cloud.common.cache.RedisService;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.pojo.book.BookChapter;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月17日 15:21
 */
@Service
public class BookChapterServiceImpl implements BookChapterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookChapterServiceImpl.class);

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisService redisService;

    @Override
    public Result<BookChapter> getChapterById(String bookId, Integer chapterId) {

        BookChapter chapter;
        String bookChapterKey = RedisBookKey.getBookChapterKey(bookId);
        String field = chapterId.toString();
        chapter = redisService.getHashVal(bookChapterKey, field, BookChapter.class);
        if (Objects.isNull(chapter)) {
            chapter = bookChapterMapper.selectById(chapterId);
            if (Objects.nonNull(chapter)) {
                this.redisService.setHashValExpire(bookChapterKey, field, chapter, RedisExpire.HOUR);
            }
        }
        return ResultUtil.success(chapter);
    }

    @Override
    public Result getBookChapterListByBookId(String bookId) {
        Book book = bookService.getBookById(bookId).getData();
        if (Objects.isNull(book)) {
            return ResultUtil.notFound().buildMessage("该书不存在于本系统哦！");
        }
        String bookChapterListKey = RedisBookKey.getBookChapterListKey(bookId);
        List<BookChapterListVO> chapterVOs = redisService.getCacheList(bookChapterListKey, BookChapter.class);

        if (Objects.isNull(chapterVOs) || chapterVOs.isEmpty()) {
            List<BookChapter> chapters = bookChapterMapper.findPageWithResult(book.getId());
            if (chapters.size() > 0) {
                chapterVOs = new ArrayList<>();
                for (int i = 0; i < chapters.size(); i++) {
                    BookChapterListVO vo = new BookChapterListVO();
                    BeanUtils.copyProperties(chapters.get(i), vo);
                    chapterVOs.add(vo);
                }
                redisService.setCacheExpire(bookChapterListKey, chapterVOs, RedisExpire.HOUR);
            }
        }
        return ResultUtil.success(chapterVOs);
    }

    @Override
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {
        Book book = bookService.getBookById(bookId).getData();
        if (Objects.isNull(book)) {
            return ResultUtil.notFound().buildMessage("该书不存在于本系统哦！");
        }
        BookChapterReadVO result = new BookChapterReadVO();
        String field = chapterId.toString();
        if (chapterId == 0) {
            field = "first";
        } else if (chapterId == -1) {
            field = "last";
        }
        BookPreviousAndNextChapterNode chapterNode = getChapterNodeData(book.getId(), field);
        if (Objects.isNull(chapterNode)) {
            // 获取不到节点数据查询首章节
            field = "first";
            chapterNode = getChapterNodeData(book.getId(), field);
            if (Objects.isNull(chapterNode)) {
                return ResultUtil.notFound().buildMessage("本书还没有任何章节内容哦！");
            }
        }
        // 获取当前章信息、内容
        String content = getChapterContent(bookId, chapterNode.getId());
        BookChapterVO current = new BookChapterVO(chapterNode.getId(),chapterNode.getName(), content);
        // 上一章、下一章
        BookChapterVO pre = null;
        BookChapterVO next = null;
        if (chapterNode.getPre() != null) {
            pre = new BookChapterVO(chapterNode.getPre().getId(), chapterNode.getPre().getName(), "");
        }
        if (chapterNode.getNext() != null) {
            next = new BookChapterVO(chapterNode.getNext().getId(), chapterNode.getNext().getName(), "");
        }

        result.setCurrent(current);
        result.setPre(pre);
        result.setNext(next);
        return ResultUtil.success(result);
    }

    /**
     * 获取章节内容
     * @author micro-paul
     * @date 2022/3/18 10:56
     * @param bookId
     * @param chapterId
     * @return java.lang.String
     */
    private String getChapterContent(String bookId, Integer chapterId){
        String content = "";
        BookChapter chapter = getChapterById(bookId, chapterId).getData();
        if (chapter != null) {
            content = chapter.getContent();
        }
        return content;
    }

    /**
     * 获取前后章节节点数据链表
     *
     * @param id
     * @param field
     * @return org.micro.reading.cloud.book.domain.BookPreviousAndNextChapterNode
     * @author micro-paul
     * @date 2022/3/17 16:39
     */
    private BookPreviousAndNextChapterNode getChapterNodeData(Integer id, String field) {
        // 缓存获取
        String key = RedisBookKey.getBookChapterNodeKey(id);
        BookPreviousAndNextChapterNode chapterNode = redisService.getHashObject(key, field, BookPreviousAndNextChapterNode.class);
        if (Objects.nonNull(chapterNode)) {
            return chapterNode;
        }
        // 章节列表
        List<BookChapter> chapterList = bookChapterMapper.findPageWithResult(id);
        if (chapterList.isEmpty()) {
            return null;
        }
        HashMap<String, BookPreviousAndNextChapterNode> map = new HashMap<>();
        // 上一章节节点数据
        BookPreviousAndNextChapterNode pre = null;
        try {
            for (int i = 1; i <= chapterList.size(); i++) {
                BookChapter chapter = chapterList.get(i - 1);
                // 锁章，获取下一章内容
                if (chapter.getLockStatus()) {
                    if (i >= chapterList.size()) {
                        break;
                    }
                    chapter = chapterList.get(i);
                }

                // 得到当前章节节点数据
                BookPreviousAndNextChapterNode curr = new BookPreviousAndNextChapterNode(chapter.getId(), chapter.getName());
                if (Objects.nonNull(pre)) {
                    curr.setPre(new BookPreviousAndNextChapterNode(pre));
                    pre.setNext(new BookPreviousAndNextChapterNode(curr));
                    // 章节id
                    map.put(pre.getId() + "", pre);
                }

                // 第二章设置前章节点数据
                if (i == 2) {
                    map.put("first", pre);
                }

                // 首章节设置当前节点数据
                if (i == 1) {
                    map.put("first", curr);
                }

                // 存储节点数据
                map.put(curr.getId() + "", curr);
                pre = curr;
            }
            // 最后一章节
            map.put("last", pre);
            redisService.setHashValExpire(key, map, RedisExpire.HOUR_FOUR);
        } catch (
                Exception e) {
            LOGGER.error("生成章节节点数据异常：{}", e);
        }
        return map.get(field);
    }
}
