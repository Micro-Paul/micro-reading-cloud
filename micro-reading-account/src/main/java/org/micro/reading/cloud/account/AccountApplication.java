package org.micro.reading.cloud.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户中心
 *
 * @author micro-paul
 * @date 2022年03月18日 11:15
 */
// @EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.micro.reading.book.feign"})
@SpringBootApplication(scanBasePackages = {"org.micro.reading.cloud.account", "org.micro.reading.cloud.common", "org.micro.reading.book.feign"})
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
