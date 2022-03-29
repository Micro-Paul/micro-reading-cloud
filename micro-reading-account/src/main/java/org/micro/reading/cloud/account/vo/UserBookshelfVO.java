package org.micro.reading.cloud.account.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author micro-paul
 * @date 2022年03月23日 16:47
 */
@Data
public class UserBookshelfVO implements Serializable {

    private Integer id;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 图书id
     */
    private String bookId;

    private String bookName;

    private String authorName;

    private String imgUrl;

    /**
     * 图书最后章节id
     */
    private Integer lastChapterId;

    /**
     * 最后阅读时间
     */
    private Long lastReadTime;
}
