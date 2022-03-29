package org.micro.reading.cloud.common.pojo.book;

import lombok.Data;

import java.util.Date;

/**
 * @author micro-paul
 * @date 2022年03月17日 15:29
 */
@Data
public class BookChapter {
    private static final Long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    protected Integer id;

    /**
     * 所属图书
     */
    private Integer bookId;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 章节内容
     */
    private String content;

    /**
     * 锁章状态(0:无,1:锁章)
     */
    private Boolean lockStatus;

    /**
     * 排序
     */
    private Integer sortNumber;

    private Date createTime;

    private Date updateTime;
}
