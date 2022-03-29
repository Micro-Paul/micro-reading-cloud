package org.micro.reading.cloud.homepage.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:38
 */
@Data
public class BannerItemVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片链接
     */
    private String imgUrl;

    /**
     * 跳转链接
     */
    private String url;
}
