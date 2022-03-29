package org.micro.reading.account.feign.client;

import org.micro.reading.account.feign.fallback.LikeSeeClientFallBack;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 喜欢看客户端feign接口
 *
 * @author micro-paul
 * @date 2022年03月23日 17:11
 */
@FeignClient(contextId = "like", name = "light-reading-cloud-account", fallbackFactory = LikeSeeClientFallBack.class)
public interface LikeSeeClient {

    /**
     * 获取书被喜欢的次数调用account里面的接口
     *
     * @param bookId
     * @return org.micro.reading.cloud.common.result.Result<java.lang.Integer>
     * @author micro-paul
     * @date 2022/3/23 17:12
     */
    @GetMapping("/account/like-see/get-count")
    Result<Integer> getBookLikesCount(@RequestParam("bookId") String bookId);
}
