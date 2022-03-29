package org.micro.reading.cloud.account.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author micro-paul
 * @date 2022年03月16日 16:52
 */
@Configuration
public class AccountCenterDataSourceConfig {
    
    /**
     * 数据源Bean
     *
     * @return javax.sql.DataSource
     * @author micro-paul
     * @date 2022/3/16 16:54
     */
    @Bean(name = "accountCenterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.account-center")
    public DataSource accountCenterDataSource() {
        return new DruidDataSource();
    }

    /**
     * 事务管理器
     *
     * @param dataSource
     * @return org.springframework.jdbc.datasource.DataSourceTransactionManager
     * @author micro-paul
     * @date 2022/3/16 16:55
     */
    @Bean(name = "bookCenterTransactionManager")
    public DataSourceTransactionManager setTransactionManager(@Qualifier("accountCenterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}
