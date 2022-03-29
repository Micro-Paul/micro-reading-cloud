package org.micro.reading.cloud.account.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 公用线程池配置
 *
 * @author micro-paul
 * @date 2022年03月22日 10:29
 */
@Configuration
@ConfigurationProperties(prefix = "spring.thread-pool.common")
@Data
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maximumPoolSize;
    /**
     * 线程存活时间
     */
    private Long keepAliveTime;
    /**
     * 队列容量
     */
    private int queueCapacity;

    @Bean(value = "commonQueueThreadPool")
    public ExecutorService buildCommonQueueThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("common-queue-thread-%d").build();
        return new ThreadPoolExecutor(
                getCorePoolSize(),
                getMaximumPoolSize(),
                getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(getQueueCapacity()),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
