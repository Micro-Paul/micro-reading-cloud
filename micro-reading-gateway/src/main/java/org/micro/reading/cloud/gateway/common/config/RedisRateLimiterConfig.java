package org.micro.reading.cloud.gateway.common.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 配置限流
 *
 * @author micro-paul
 * @date 2022年03月21日 10:18
 */
@Configuration
public class RedisRateLimiterConfig {

    /**
     * 按客户端IP限流
     *
     * @return
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}