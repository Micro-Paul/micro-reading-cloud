package org.micro.reading.cloud.homepage.common.config;

import com.netflix.hystrix.*;
import feign.Feign;
import feign.hystrix.HystrixFeign;
import org.micro.reading.book.feign.client.BookClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 图书资源中心 - 图书服务熔断配置
 *
 * @author micro-paul
 * @date 2022年03月24日 10:05
 */
@Configuration
public class HystrixCommandBookConfig {


    @Bean
    public Feign.Builder bookFeignHystrixBuilder() {
        return HystrixFeign.builder().setterFactory((target, method) -> HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(BookClient.class.getSimpleName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(BookClient.class.getSimpleName()))
                .andCommandPropertiesDefaults(// 超时配置
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withCoreSize(3)
                        .withMaximumSize(5)
                        .withMaxQueueSize(30)
                )
        );
    }
}
