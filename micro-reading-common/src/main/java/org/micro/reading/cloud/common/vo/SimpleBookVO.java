package org.micro.reading.cloud.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 简单图书VO
 * @author micro-paul
 * @date 2022年03月15日 17:55
 */
@Data
public class SimpleBookVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 作者
     */
    private Integer authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 封面
     */
    private String imgUrl;

    /**
     * 图书评分
     */
    private Integer bookScore;

    /**
     * 简介
     */
    private String introduction;

}
