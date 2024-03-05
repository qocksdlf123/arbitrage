package com.arbitrage;

import com.arbitrage.common.service.ArbitrageService;
import com.arbitrage.domain.upbit.service.UpbitPairService;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ArbitrageApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(ArbitrageApplication.class);

        UpbitPairService upbitPairService = context.getBean(UpbitPairService.class);
        ArbitrageService arbitrageService = context.getBean(ArbitrageService.class);


//        upbitPairService.saveUpbitPair();
        Double orderbookVolume = upbitPairService.getOrderbookVolume(new CurrencyPair("AVAX/KRW"), true);
        System.out.println(String.format("%.4f",orderbookVolume) );

        String refreshToken = arbitrageService.getRefreshToken();
        System.out.println(refreshToken);
    }

}
