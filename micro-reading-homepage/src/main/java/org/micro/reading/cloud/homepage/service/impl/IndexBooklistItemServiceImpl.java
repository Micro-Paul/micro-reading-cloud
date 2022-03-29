package org.micro.reading.cloud.homepage.service.impl;

import org.micro.reading.account.feign.client.LikeSeeClient;
import org.micro.reading.cloud.common.constant.CategoryConstant;
import org.micro.reading.cloud.common.enums.BookSerialStatusEnum;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.homepage.service.BookCenterService;
import org.micro.reading.cloud.homepage.service.IndexBooklistItemService;
import org.micro.reading.cloud.homepage.vo.BooklistBookVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月24日 17:10
 */
@Service
public class IndexBooklistItemServiceImpl implements IndexBooklistItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexBooklistItemServiceImpl.class);

    @Autowired
    private BookCenterService bookCenterService;

    @Autowired
    private LikeSeeClient likeSeeClient;

    @Override
    public Result getBooklistPagingBooks(Integer booklistId, Integer page, Integer limit) {
        return null;
    }

    @Override
    public Result getBooklistExchange(Integer booklistId, Integer clientRandomNumber) {
        return null;
    }

    @Override
    public List<BooklistBookVO> getBooklistRandomBooks(Integer booklistId, String bookIds, Integer showNumber, Integer clientRandomNumber, Boolean showLikeCount) {
        return null;
    }

    @Override
    public List<BooklistBookVO> getBooklistOrderBooks(Integer booklistId, String bookIds, Integer showNumber, Boolean showLikeCount) {
        String[] bookIdArray = bookIds.split(",");
        return this.getBooklistBookVOByBookIdArray(bookIdArray, showNumber, showLikeCount);
    }

    private List<BooklistBookVO> getBooklistBookVOByBookIdArray(String[] bookIdArray, Integer showNumber, Boolean showLikeCount) {
        List<BooklistBookVO> vos = new ArrayList<>();
        for (int i = 0; i < bookIdArray.length; i++) {
            String bookId = bookIdArray[i];
            BooklistBookVO vo = this.getBookVO(bookId);
            if (vo != null) {
                // 获取喜欢数
                if (showLikeCount) {
                    Integer likeCount = this.likeSeeClient.getBookLikesCount(bookId).getData();
                    vo.setLikeCount(likeCount);
                }
                vos.add(vo);
            }

            // VOS到达榜单定制数量，不再获取了
            if (vos.size() == showNumber) {
                break;
            }
        }
        return vos;
    }

    private BooklistBookVO getBookVO(String bookId) {
        Book book = this.bookCenterService.getBookById(bookId);
        if (book == null) {
            LOGGER.warn("图书资源中心获取Book:{}失败！返回了空数据", bookId);
            return null;
        }
        BooklistBookVO vo = new BooklistBookVO();
        BeanUtils.copyProperties(book, vo);
        vo.setLikeCount(0);
        // 分类
        String categoryName = CategoryConstant.categorys.get(book.getDicCategory());
        vo.setCategoryName(categoryName);
        // 连载状态
        String serialStatusName = BookSerialStatusEnum.values()[book.getDicSerialStatus() - 1].getName();
        vo.setSerialStatusName(serialStatusName);
        return vo;
    }
}
