package com.my.interrior;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class IDesignApplication {

    public static void main(String[] args){
        SpringApplication.run(IDesignApplication.class, args);
    }
}