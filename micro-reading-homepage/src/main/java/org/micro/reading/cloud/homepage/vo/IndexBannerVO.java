package org.micro.reading.cloud.homepage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexBannerVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private List<BannerItemVO> items;
}
