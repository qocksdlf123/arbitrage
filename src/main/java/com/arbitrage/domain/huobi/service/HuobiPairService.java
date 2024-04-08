package com.arbitrage.domain.huobi.service;

import com.arbitrage.domain.huobi.dao.HuobiPairRepository;
import com.arbitrage.domain.huobi.domain.HuobiPair;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.huobi.HuobiExchange;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuobiPairService {
    Exchange Huobi = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class);
    MarketDataService marketDataService = Huobi.getMarketDataService();
    List<Instrument> exchangeInstruments = Huobi.getExchangeInstruments();

    private final HuobiPairRepository huobiPairRepository;

    private final RestTemplate restTemplate;

    public void saveHuobiPair() {      // 1회성 pair 쌍 저장 메서드
        log.info("exchangeInstruments : {}", exchangeInstruments);
        log.info("exchangeInstruments 개수 : {}", exchangeInstruments.size());
        exchangeInstruments.forEach((Instrument i) -> {
            HuobiPair huobiPair = HuobiPair.builder()
                    .tokenSymbol(i.getBase().toString())
                    .currency(i.getCounter().toString())
                    .build();
            huobiPairRepository.save(huobiPair);

            log.info("Instrument : {} {}", i.getBase(), i.getCounter());
        });
    }

    public void addHuobiTokenName() {
        List<HuobiPair> HuobiPairs = huobiPairRepository.findAll();
        HuobiPairs.forEach((HuobiPair HuobiPair) -> {
            // 구현 예정
        });
    }

    public List<Instrument> getCurrencyList() {
        return exchangeInstruments;
    }

    //    public Double currentPrice(CurrencyPair currencyPair) throws IOException {
//        String marketCode = currencyPair.getCounter().toString().toUpperCase()
//                + "-"
//                +currencyPair.getBase().toString().toUpperCase();
//        log.info("marketCode : {}",marketCode);
//        String tickerUrl = "https://api.Huobi.com/v1/ticker?markets=" + marketCode.toString();
////        HuobiCurrencyRequestDto dto = restTemplate.getForObject(tickerUrl,HuobiCurrencyRequestDto.class);
//        String dto = restTemplate.getForObject(tickerUrl,String.class);
//        log.info("last price : {}",dto);
//        Double lastPrice = 0D;
//
//        return lastPrice;
//    }
    public Double currentPrice(CurrencyPair currencyPair) throws IOException {

        Ticker ticker = marketDataService.getTicker(currencyPair);
        return ticker.getLast().doubleValue();
    }
    public Double getOrderbookVolume(CurrencyPair currencyPair, Boolean isask) throws IOException {
        Double currentPrice = currentPrice(currencyPair);
        Double totalTokenAmount = 0D;
        OrderBook orderBook = marketDataService.getOrderBook(currencyPair);
        log.info("currentPrice : {}",currentPrice);

        //ask : 매도 호가창 bid : 매수 호가창
        if (isask == true) {
            List<LimitOrder> asks = orderBook.getAsks();
            for (LimitOrder ask : asks) {
                log.info("ask : {}",ask);
                Double limitPrice = ask.getLimitPrice().doubleValue();
                if (limitPrice <= currentPrice * 1.03) {
                    totalTokenAmount += ask.getOriginalAmount().doubleValue();
                }
            }
        } else if (isask == false) {
            List<LimitOrder> bids = orderBook.getBids();
            for (LimitOrder bid : bids) {
                Double limitPrice = bid.getLimitPrice().doubleValue();
                if (limitPrice >= currentPrice * 0.97) {
                    totalTokenAmount += bid.getOriginalAmount().doubleValue();
                }
            }
        }
        String currency = currencyPair.getCounter().toString();

        Double Multiple = 0D;
            log.info("currency : {}",currency);
        if (currency.equals("KRW")) {
            Multiple = 1D;
        } else if (currency.equals("BTC")) {
            Multiple = 90000000D; // 추후 비트코인 가격을 통해 값 받아올 것
        } else if (currency.equals("USDT")) {
            Multiple = 1300D;       //추후 원달러 환율 API를 통해 값 받아올 것
        }
        log.info("totalTokenAmount : {}",totalTokenAmount);
        log.info("totalTokenAmount * currentPrice : {}",totalTokenAmount * currentPrice);
//        log.info("Multiple : {}", Multiple);
        return totalTokenAmount * currentPrice * Multiple;
    }

    public Integer[] getDWStatus(String currency){
        String url = "https://api.huobi.pro/v2/reference/currencies?currency=" + currency;
        log.info("url : {}, ", url);
        JsonNode node = restTemplate.getForObject(url, JsonNode.class);
        log.info("nod : {}",node);
        log.info("currency : {}",currency);
        String depositStatus = node.get("data").get(0).get("chains").get(0).get("depositStatus").asText();
        String withdrawalStatus = node.get("data").get(0).get("chains").get(0).get("withdrawStatus").asText();
        Integer[] status = new Integer[2];
        if(depositStatus.equals("allowed")) status[0] = 1;
        else if (depositStatus.equals("prohibited")) status[0] = 0;
        if(withdrawalStatus.equals("allowed")) status[1] = 1;
        else if (withdrawalStatus.equals("prohibited")) status[1] = 0;

        return status;
    }
    @Scheduled(cron = "0 */10 * * * *")
    void getOrderBook() throws IOException {
        log.info("1");
        CurrencyPair currencyPair = new CurrencyPair("LBL/USDT");
        Double currentPrice = currentPrice(currencyPair);
        Double totalTokenAmount = 0D;
        OrderBook orderBook = marketDataService.getOrderBook(currencyPair);
        log.info("currentPrice : {}", currentPrice);

        //ask : 매도 호가창 bid : 매수 호가창

        List<LimitOrder> asks = orderBook.getAsks();
        for (LimitOrder ask : asks) {
            Double limitPrice = ask.getLimitPrice().doubleValue();
            if (limitPrice <= currentPrice * 1.05) {
                totalTokenAmount += ask.getOriginalAmount().doubleValue();
            }
        }

        String currency = currencyPair.getCounter().toString();

        Double Multiple = 0D;
        log.info("currency : {}", currency);
        if (currency.equals("KRW")) {
            Multiple = 1D;
        } else if (currency.equals("BTC")) {
            Multiple = 90000000D; // 추후 비트코인 가격을 통해 값 받아올 것
        } else if (currency.equals("USDT")) {
            Multiple = 1300D;       //추후 원달러 환율 API를 통해 값 받아올 것
        }
        log.info("totalTokenAmount : {}", totalTokenAmount);
        log.info("totalTokenAmount * currentPrice : {}", totalTokenAmount * currentPrice);
//        log.info("Multiple : {}", Multiple);

    }
}
