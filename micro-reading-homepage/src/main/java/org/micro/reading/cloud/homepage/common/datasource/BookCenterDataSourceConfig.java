package org.micro.reading.cloud.homepage.common.datasource;

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
public class BookCenterDataSourceConfig {
    
    /**
     * 数据源Bean
     *
     * @return javax.sql.DataSource
     * @author micro-paul
     * @date 2022/3/16 16:54
     */
    @Bean(name = "bookCenterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.book-center")
    public DataSource bookCenterDatasource() {
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
    public DataSourceTransactionManager setTransactionManager(@Qualifier("bookCenterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}
