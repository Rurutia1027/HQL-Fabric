package com.hql.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hql.fabric")
public class HqlFabricExampleTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(HqlFabricExampleTestApplication.class, args);
    }
}
