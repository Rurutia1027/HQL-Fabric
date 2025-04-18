package com.hql.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hql.fabric")
public class HqlFabricExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(HqlFabricExampleApplication.class, args);
    }
}
