package com.baldprogrammer.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baldprogrammer.crm.dao")
public class CRMApplication {
    public static void main(String[] args) {
        SpringApplication.run(CRMApplication.class);
    }
}
