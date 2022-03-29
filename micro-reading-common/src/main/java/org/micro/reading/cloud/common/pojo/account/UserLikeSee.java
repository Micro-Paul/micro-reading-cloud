package org.micro.reading.cloud.common.pojo.account;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author micro-paul
 * @date 2022年03月18日 15:59
 */
@Data
public class UserLikeSee implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 创建时间
     */
    private Date createTime;
}
