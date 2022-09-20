package com.Lmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@MapperScan("com.Lmall.dao")
public class ShoppingCentreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCentreApplication.class, args);
    }

}
