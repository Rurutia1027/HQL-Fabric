
package com.hql.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hql.fabric")
public class HqlFabricRoutingApplication {
    public static void main(String[] args) {
        SpringApplication.run(HqlFabricRoutingApplication.class, args);
    }
}