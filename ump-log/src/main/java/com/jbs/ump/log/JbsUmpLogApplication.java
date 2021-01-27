package com.jbs.ump.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:spring-beans.xml"})
public class JbsUmpLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(JbsUmpLogApplication.class, args);
    }

}
