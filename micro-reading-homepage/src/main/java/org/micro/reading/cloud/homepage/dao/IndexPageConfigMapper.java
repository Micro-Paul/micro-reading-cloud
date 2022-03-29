package org.micro.reading.cloud.homepage.dao;

import org.micro.reading.cloud.common.pojo.index.IndexPageConfig;

import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:32
 */
public interface IndexPageConfigMapper {

    /***
     * 查询主页配置
     * @param type
     * @return
     */
    List<IndexPageConfig> findPageWithResult(Integer type);
}
