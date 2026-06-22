package com.example.tripagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.tripagent.mapper")
public class TripAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripAgentApplication.class, args);
    }

}
