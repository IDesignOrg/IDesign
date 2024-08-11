package com.my.interrior;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // schedule 사용
public class IDesignApplication {

    public static void main(String[] args){
        SpringApplication.run(IDesignApplication.class, args);
    }
}