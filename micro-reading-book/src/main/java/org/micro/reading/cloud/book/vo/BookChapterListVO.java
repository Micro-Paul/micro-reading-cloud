package org.micro.reading.cloud.book.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author micro-paul
 * @date 2022年03月17日 16:17
 */
@Data
public class BookChapterListVO  implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** 主键ID */
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
     * 锁章状态(0:无,1:锁章)
     */
    private Boolean lockStatus;

    private Integer sortNumber;
}
