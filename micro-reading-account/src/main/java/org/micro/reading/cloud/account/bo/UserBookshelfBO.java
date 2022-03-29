package org.micro.reading.cloud.account.bo;

import lombok.Data;

/**
 * @author micro-paul
 * @date 2022年03月22日 9:45
 */
@Data
public class UserBookshelfBO {

    private Integer id;

    /**
     * 同步类型：
     * 1.新增  2.更新  3.删除
     */
    private int syncType;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 最后章节ID
     */
    private Integer lastChapterId;
}
