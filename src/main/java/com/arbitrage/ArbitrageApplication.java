package com.arbitrage;

import com.arbitrage.common.service.ArbitrageService;
import com.arbitrage.domain.upbit.service.UpbitPairService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ArbitrageApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(ArbitrageApplication.class);

        UpbitPairService upbitPairService = context.getBean(UpbitPairService.class);
        ArbitrageService arbitrageService = context.getBean(ArbitrageService.class);


//        upbitPairService.saveUpbitPair();
        Double orderbookVolume = upbitPairService.getOrderbookVolume(new CurrencyPair("AVAX/KRW"), true);
        System.out.println(String.format("%.4f",orderbookVolume) );

//        String refreshToken = arbitrageService.getRefreshToken();
//        System.out.println(refreshToken);

//        arbitrageService.getDAW();

        List<Instrument> currencyList = upbitPairService.getCurrencyList();
        currencyList.forEach((i) -> {
            Currency base = i.getBase();
            Currency counter = i.getCounter();
            CurrencyPair currencypair = new CurrencyPair(base.toString() + "/" + counter.toString());
            Double currentPrice = 0D;
            try {
                currentPrice = upbitPairService.currentPrice(currencypair);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                log.info("base : {} , counter : {}",base,counter);
                log.info("currentPrice : {}",currentPrice);
            }
        });
    }


}
