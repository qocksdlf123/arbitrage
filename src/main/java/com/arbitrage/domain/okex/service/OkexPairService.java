package com.arbitrage.domain.Okex.service;

import com.arbitrage.domain.okex.dao.OkexPairRepository;
import com.arbitrage.domain.okex.domain.OkexPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okex.OkexExchange;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkexPairService {
    Exchange Okex = ExchangeFactory.INSTANCE.createExchange(OkexExchange.class);
    MarketDataService marketDataService = Okex.getMarketDataService();
    List<Instrument> exchangeInstruments = Okex.getExchangeInstruments();

    private final OkexPairRepository okexPairRepository;

    private final RestTemplate restTemplate;

    public void saveOkexPair() {      // 1회성 pair 쌍 저장 메서드
        log.info("exchangeInstruments : {}", exchangeInstruments);
        log.info("exchangeInstruments 개수 : {}", exchangeInstruments.size());
        exchangeInstruments.forEach((Instrument i) -> {
            OkexPair okexPair = OkexPair.builder()
                    .tokenSymbol(i.getBase().toString())
                    .currency(i.getCounter().toString())
                    .build();
            okexPairRepository.save(okexPair);

            log.info("Instrument : {} {}", i.getBase(), i.getCounter());
        });
    }

    public void addOkexTokenName() {
        List<OkexPair> OkexPairs = okexPairRepository.findAll();
        OkexPairs.forEach((OkexPair OkexPair) -> {
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
//        String tickerUrl = "https://api.Okex.com/v1/ticker?markets=" + marketCode.toString();
////        OkexCurrencyRequestDto dto = restTemplate.getForObject(tickerUrl,OkexCurrencyRequestDto.class);
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
                if (limitPrice <= currentPrice * 1.05) {
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
        log.info("Multiple : {}", Multiple);
        return totalTokenAmount * currentPrice * Multiple;
    }
}
