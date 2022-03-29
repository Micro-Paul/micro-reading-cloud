package org.micro.reading.cloud.account.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author micro-paul
 * @date 2022年03月22日 9:56
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.thread-pool.bookshelf")
public class BookshelfThreadPoolConfig {
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

    @Bean
    public ExecutorService buildUserBookshelfQueueThreadPool() {

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("user-bookshelf-queue-thread-%d").build();
        // 实例化线程池
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
