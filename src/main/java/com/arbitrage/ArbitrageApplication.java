package com.arbitrage;

import com.arbitrage.common.service.ArbitrageService;
import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import com.arbitrage.domain.okex.service.OkexPairService;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ArbitrageApplication.class);

        HuobiPairService huobiPairService = context.getBean(HuobiPairService.class);
        OkexPairService okexPairService = context.getBean(OkexPairService.class);
        ArbitrageService arbitrageService = context.getBean(ArbitrageService.class);
        BithumbPairService bithumbPairService = context.getBean(BithumbPairService.class);


//        huobiPairService.saveHuobiPair();
//        bithumbPairService.saveBithumbPair();
//        Double orderbookVolume = huobiPairService.getOrderbookVolume(new CurrencyPair("AVAX/KRW"), true);
//        System.out.println(String.format("%.4f", orderbookVolume));

//        String refreshToken = arbitrageService.getRefreshToken();
//        System.out.println(refreshToken);

//        arbitrageService.getDAW();

        List<Instrument> currencyList = huobiPairService.getCurrencyList();
        log.info("currencyList : {}",currencyList);
        Integer callNumber = 0;
//        for (Instrument i : currencyList) {
//
//
//            if(callNumber == 9) {
//                Thread.sleep(1000);
//                callNumber = 0;
//            }
//            Currency base = i.getBase();
//            Currency counter = i.getCounter();
//            if(!counter.toString().equals("USDT")){
//                continue;
//            }
//            CurrencyPair currencypair = new CurrencyPair(base.toString() + "/" + counter.toString());
//            Double currentPrice = 0D;
//            try {
//                currentPrice = huobiPairService.currentPrice(currencypair);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                log.info("base : {} , counter : {}", base, counter);
//                log.info("currentPrice : {}", currentPrice);
//            }
//            callNumber++;
//        }
        Double orderbookVolume = huobiPairService.getOrderbookVolume(new CurrencyPair("ZBC/USDT"), true);
        log.info("orderbookVolume : {}",orderbookVolume );

//        Double orderbookVolume = okexPairService.getOrderbookVolume(new CurrencyPair("ID/USDT"), true);
//        log.info("orderbookVolume : {}",orderbookVolume );

        Integer[] eths = bithumbPairService.getDWStatus("Eth");
        log.info("deposit : {}, withdrawal : {}",eths[0],eths[1]);

        Integer[] eths1 = huobiPairService.getDWStatus("ETH");
        log.info("deposit : {}, withdrawal : {}",eths1[0],eths1[1]);


    }


}
