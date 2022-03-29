package org.micro.reading.cloud.homepage.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:36
 */
@Data
public class IndexPageVO implements Serializable {
    private static final Long serialVersionUID = 1L;

    /**
     * 配置项类型：1.banner,2.榜单
     */
    private Integer itemType;

    /**
     * 榜单/banner id
     */
    private Integer itemId;

    /**
     * Banner VO对象
     */
    private IndexBannerVO banner;

    /**
     * 榜单VO对象
     */
    private IndexBooklistVO booklist;
}
