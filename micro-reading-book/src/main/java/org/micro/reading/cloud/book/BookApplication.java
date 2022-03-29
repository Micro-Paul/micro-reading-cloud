package org.micro.reading.cloud.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 图书资源中心
 *
 * @author micro-paul
 * @date 2022年03月16日 15:07
 */
// @EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"org.micro.reading.cloud.book", "org.micro.reading.cloud.common"})
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
