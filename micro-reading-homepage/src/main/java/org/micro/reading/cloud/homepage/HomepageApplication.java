package org.micro.reading.cloud.homepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author micro-paul
 * @date 2022年03月23日 17:16
 */
@EnableFeignClients(basePackages = {"org.micro.reading.book.feign", "org.micro.reading.account.feign"})
@SpringBootApplication(scanBasePackages = {"org.micro.reading.cloud.homepage", "org.micro.reading.cloud.common", "org.micro.reading.book.feign", "org.micro.reading.account.feign"})
@EnableHystrix
@EnableHystrixDashboard
public class HomepageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomepageApplication.class, args);
    }
}
