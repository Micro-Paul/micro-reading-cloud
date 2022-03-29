package org.micro.reading.cloud.gateway.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author micro-paul
 * @date 2022年03月21日 10:18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.properties")
public class SystemPropertiesConfig {
    /**
     * 请求白名单
     */
    private String whitelist;
}
