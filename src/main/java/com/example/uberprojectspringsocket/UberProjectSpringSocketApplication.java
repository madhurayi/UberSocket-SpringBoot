package com.example.uberprojectspringsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.example.uberprojectentityservice.models")
public class UberProjectSpringSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberProjectSpringSocketApplication.class, args);
    }

}
