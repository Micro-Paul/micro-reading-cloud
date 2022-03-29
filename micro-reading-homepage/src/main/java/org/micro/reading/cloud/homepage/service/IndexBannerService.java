package org.micro.reading.cloud.homepage.service;

import org.micro.reading.cloud.homepage.vo.IndexBannerVO;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:58
 */
public interface IndexBannerService {

    IndexBannerVO getBannerVO(Integer itemId);
}
