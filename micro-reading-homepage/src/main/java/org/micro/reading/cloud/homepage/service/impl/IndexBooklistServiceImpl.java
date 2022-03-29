package org.micro.reading.cloud.homepage.service.impl;

import org.micro.reading.cloud.common.cache.RedisExpire;
import org.micro.reading.cloud.common.cache.RedisHomepageKey;
import org.micro.reading.cloud.common.cache.RedisService;
import org.micro.reading.cloud.common.enums.BooklistMoreTypeEnum;
import org.micro.reading.cloud.common.pojo.index.IndexBooklist;
import org.micro.reading.cloud.homepage.service.IndexBooklistItemService;
import org.micro.reading.cloud.homepage.service.IndexBooklistService;
import org.micro.reading.cloud.homepage.vo.BooklistBookVO;
import org.micro.reading.cloud.homepage.vo.IndexBooklistVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月24日 16:00
 */
@Service
public class IndexBooklistServiceImpl implements IndexBooklistService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IndexBooklistItemService indexBooklistItemService;

    @Override
    public IndexBooklistVO getIndexBooklistVO(Integer itemId, Integer clientRandomNumber) {
        IndexBooklist booklist = this.getIndexBooklistById(itemId);
        // 是否随机获取
        boolean randomFlag = booklist.getMoreType() == BooklistMoreTypeEnum.EXCHANGE.getValue() ? true : false;
        IndexBooklistVO booklistVO;
        if (randomFlag) {
            booklistVO = this.getRandomIndexBooklistVO(booklist, clientRandomNumber);
        } else {
            String key = RedisHomepageKey.getBooklistVoKey(itemId);
            booklistVO = this.redisService.getCache(key, IndexBooklistVO.class);
            if (Objects.isNull(booklistVO)) {
                // DB 顺序获取
                List<BooklistBookVO> books =  this.indexBooklistItemService.getBooklistOrderBooks(booklist.getId(), booklist.getBookIds(), booklist.getShowNumber(), booklist.getShowLikeCount());
                if (books.size() > 0) {
                    booklistVO = new IndexBooklistVO();
                    BeanUtils.copyProperties(booklist, booklistVO);
                    booklistVO.setBooks(books);
                    booklistVO.setRandomNumber(1);
                    this.redisService.setCacheExpire(key, booklistVO, RedisExpire.HOUR_TWO);
                }
            }
        }
        return booklistVO;
    }

    @Override
    public IndexBooklist getIndexBooklistById(Integer booklistId) {
        return null;
    }

    @Override
    public IndexBooklistVO getRandomIndexBooklistVO(IndexBooklist booklist, Integer clientRandomNumber) {
        return null;
    }
}
