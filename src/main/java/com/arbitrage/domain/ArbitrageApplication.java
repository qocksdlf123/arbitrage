package com.arbitrage.domain;

import com.arbitrage.domain.upbit.service.UpbitPairService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ArbitrageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ArbitrageApplication.class);
        UpbitPairService upbitPairService = context.getBean(UpbitPairService.class);
        upbitPairService.saveUpbitPair();


    }

}
