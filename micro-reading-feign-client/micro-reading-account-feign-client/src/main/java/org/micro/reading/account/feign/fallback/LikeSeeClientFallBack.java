package org.micro.reading.account.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.micro.reading.account.feign.client.LikeSeeClient;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author micro-paul
 * @date 2022年03月23日 17:13
 */
@Component
public class LikeSeeClientFallBack implements FallbackFactory<LikeSeeClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeSeeClientFallBack.class);

    @Override
    public LikeSeeClient create(Throwable throwable) {
        return new LikeSeeClient() {
            @Override
            public Result<Integer> getBookLikesCount(String bookId) {
                LOGGER.error("获取喜欢看[{}]数量失败：{}", bookId, throwable.getMessage());
                return ResultUtil.success(0);
            }
        };
    }
}
