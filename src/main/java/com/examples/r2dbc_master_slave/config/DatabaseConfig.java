package com.examples.r2dbc_master_slave.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import java.time.Duration;

@Configuration
public class DatabaseConfig {
    @Value("${spring.r2dbc.master.url}")
    private String masterDbURL;

    @Value("${spring.r2dbc.slave.url}")
    private String slaveDbURL;

    @Bean(name = "readerConnectionFactory")
    @Primary
    public ConnectionFactory readerConnectionFactory() {
        return getConnectionPool(slaveDbURL);
    }

    @Bean(name = "writerConnectionFactory")
    public ConnectionFactory primaryConnectionFactory() {
        return getConnectionPool(masterDbURL);
    }

    @Bean(name = "readerR2dbcEntityTemplate")
    @Primary
    public R2dbcEntityTemplate readerR2dbcEntityTemplate(@Qualifier(value = "readerConnectionFactory") ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean(name = "writerR2dbcEntityTemplate")
    public R2dbcEntityTemplate writerR2dbcEntityTemplate(@Qualifier(value = "writerConnectionFactory") ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }


    private ConnectionPool getConnectionPool(String url) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory).build();
        return new ConnectionPool(configuration);
    }

}