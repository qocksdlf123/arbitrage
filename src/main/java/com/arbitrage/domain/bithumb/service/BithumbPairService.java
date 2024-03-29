package com.arbitrage.domain.bithumb.service;

import com.arbitrage.domain.bithumb.dao.BithumbPairRepository;
import com.arbitrage.domain.bithumb.domain.BithumbPair;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bithumb.BithumbExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.bithumb.BithumbExchange;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BithumbPairService {
    Exchange Bithumb = ExchangeFactory.INSTANCE.createExchange(BithumbExchange.class);
    MarketDataService marketDataService = Bithumb.getMarketDataService();
    List<Instrument> exchangeInstruments = Bithumb.getExchangeInstruments();


    private final BithumbPairRepository BithumbPairRepository;
    private final RestTemplate restTemplate;

    public void saveBithumbPair() {      // 1회성 pair 쌍 저장 메서드
        log.info("exchangeInstruments : {}", exchangeInstruments);
        log.info("exchangeInstruments 개수 : {}", exchangeInstruments.size());
        exchangeInstruments.forEach((Instrument i) -> {
            BithumbPair bithumbPair = BithumbPair.builder()
                    .tokenSymbol(i.getBase().toString())
                    .currency(i.getCounter().toString())
                    .build();
            BithumbPairRepository.save(bithumbPair);

            log.info("Instrument : {} {}", i.getBase(), i.getCounter());
        });
    }

    public void addBithumbTokenName() {
        List<BithumbPair> BithumbPairs = BithumbPairRepository.findAll();
        BithumbPairs.forEach((BithumbPair BithumbPair) -> {
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
//        String tickerUrl = "https://api.Bithumb.com/v1/ticker?markets=" + marketCode.toString();
////        BithumbCurrencyRequestDto dto = restTemplate.getForObject(tickerUrl,BithumbCurrencyRequestDto.class);
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
        JsonNode node = restTemplate.getForObject("https://api.bithumb.com/public/assetsstatus/multichain/" + currency, JsonNode.class);
        Integer depositStatus = node.get("data").get(0).get("deposit_status").asInt();
        Integer withdrawalStatus = node.get("data").get(0).get("withdrawal_status").asInt();
        return new Integer[] {depositStatus, withdrawalStatus};

    }



}
