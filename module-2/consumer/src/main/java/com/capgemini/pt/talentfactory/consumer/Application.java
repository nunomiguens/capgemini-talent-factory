package com.capgemini.pt.talentfactory.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan // scans for @Component beans
@EnableAutoConfiguration
@EntityScan(basePackages = "com.capgemini.pt.talentfactory.consumer")
public class Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Application.class);
        logger.info("Starting application consumer.");
        SpringApplication.run(Application.class, args);
    }
}
