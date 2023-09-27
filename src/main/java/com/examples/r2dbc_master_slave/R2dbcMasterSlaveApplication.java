package com.examples.r2dbc_master_slave;

import com.examples.r2dbc_master_slave.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;

@SpringBootApplication
@Slf4j
public class R2dbcMasterSlaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcMasterSlaveApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(R2dbcEntityTemplate readerEntityTemplate, @Qualifier(value = "writerR2dbcEntityTemplate") R2dbcEntityTemplate writerEntityTemplate) {
       return args -> {

           readerEntityTemplate.select(Query.empty(), Customer.class)
                   .doOnNext(customer -> log.info(customer.toString()))
                   .blockLast();

           writerEntityTemplate.insert(Customer.class)
                   .into("customer")
                   .using(new Customer(null, "fistName", "lastName"))
                   .doOnNext(customer -> log.info(customer.toString()))
                   .block();
       };
    }
}
