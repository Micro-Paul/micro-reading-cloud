package org.micro.reading.cloud.homepage.service;

import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:30
 */
public interface IndexPageConfigService {

    Result getIndexPageByType(Integer type, Integer page, Integer limit);
}
