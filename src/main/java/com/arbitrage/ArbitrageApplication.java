package com.arbitrage;

import com.arbitrage.common.service.BithumbHuobiServcie;
import com.arbitrage.common.service.ThettariService;
import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import com.arbitrage.domain.okex.service.OkexPairService;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.instrument.Instrument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class ArbitrageApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ArbitrageApplication.class);

        HuobiPairService huobiPairService = context.getBean(HuobiPairService.class);
        OkexPairService okexPairService = context.getBean(OkexPairService.class);
        ThettariService thettariService = context.getBean(ThettariService.class);
        BithumbPairService bithumbPairService = context.getBean(BithumbPairService.class);
        BithumbHuobiServcie bithumbHuobiServcie = context.getBean(BithumbHuobiServcie.class);
        bithumbHuobiServcie.compare();

//        huobiPairService.saveHuobiPair();
//        bithumbPairService.saveBithumbPair();
//        Double orderbookVolume = huobiPairService.getOrderbookVolume(new CurrencyPair("AVAX/KRW"), true);
//        System.out.println(String.format("%.4f", orderbookVolume));

//        String refreshToken = arbitrageService.getRefreshToken();
//        System.out.println(refreshToken);

//        arbitrageService.getDAW();

//        List<Instrument> currencyList = huobiPairService.getCurrencyList();
//        log.info("currencyList : {}",currencyList);
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
//        Double orderbookVolume = huobiPairService.getOrderbookVolume(new CurrencyPair("ZBC/USDT"), true);
//        log.info("orderbookVolume : {}",orderbookVolume );
//
////        Double orderbookVolume = okexPairService.getOrderbookVolume(new CurrencyPair("ID/USDT"), true);
////        log.info("orderbookVolume : {}",orderbookVolume );
//
//        Integer[] eths = bithumbPairService.getDWStatus("Eth");
//        log.info("deposit : {}, withdrawal : {}",eths[0],eths[1]);
//
//        Integer[] eths1 = huobiPairService.getDWStatus("eth");
//        log.info("deposit : {}, withdrawal : {}",eths1[0],eths1[1]);
//
//        Exchange bithumb = ExchangeFactory.INSTANCE.createExchange(BithumbExchange.class);
//        List<Instrument> bithumbCurrencyList = bithumb.getExchangeInstruments();
//        log.info("bithumbCurrencyList size : {}", bithumbCurrencyList.size());
//        bithumbCurrencyList.forEach((i)->{
//            log.info("빗썸 Instrument : {}", i);
//        });
//
//        Double aDouble = bithumbPairService.getOrderbookVolume(new CurrencyPair("ETH/KRW"), true);
//        log.info("aDouble : {}",aDouble);
    }

}
