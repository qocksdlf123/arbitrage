package com.arbitrage.domain.upbit.service;

import com.arbitrage.domain.upbit.dao.UpbitPairRepository;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.upbit.UpbitExchange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitPairService {
    Exchange upbit = ExchangeFactory.INSTANCE.createExchange(UpbitExchange.class);
    MarketDataService marketDataService = upbit.getMarketDataService();
    List<Instrument> exchangeInstruments = upbit.getExchangeInstruments();
    private final UpbitPairRepository upbitPairRepository;

    public void saveUpbitPair() {      // 1회성 pair 쌍 저장 메서드
        log.info("exchangeInstruments : {}", exchangeInstruments);
        log.info("exchangeInstruments 개수 : {}", exchangeInstruments.size());
        exchangeInstruments.forEach((Instrument i) -> {
            UpbitPair upbitPair = UpbitPair.builder()
                    .tokenSymbol(i.getBase().toString())
                    .currency(i.getCounter().toString())
                    .build();
            upbitPairRepository.save(upbitPair);

            log.info("Instrument : {} {}", i.getBase(), i.getCounter());
        });
    }

    public void addUpbitTokenName() {
        List<UpbitPair> upbitPairs = upbitPairRepository.findAll();
        upbitPairs.forEach((UpbitPair upbitPair) -> {
            // 구현 예정
        });
    }

    public List<Instrument> getCurrencyList() {
        return exchangeInstruments;
    }

    public Double currentPrice(CurrencyPair currencyPair) throws IOException {
        Ticker ticker = marketDataService.getTicker(currencyPair);
        Double lastPrice = ticker.getLast().doubleValue();
        return lastPrice;
    }

    public Double getOrderbookVolume(CurrencyPair currencyPair, Boolean isask) throws IOException {
        Double currentPrice = currentPrice(currencyPair);
        Double totalTokenAmount = 0D;
        OrderBook orderBook = marketDataService.getOrderBook(currencyPair);

        //ask : 매도 호가창 bid : 매수 호가창
        if (isask == true) {
            List<LimitOrder> asks = orderBook.getAsks();
            for (LimitOrder ask : asks) {
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
        if (currency.equals("KRW")) {
            Multiple = 1D;
        } else if (currency.equals("BTC")) {
            Multiple = 1 / 90000000D; // 추후 비트코인 가격을 통해 값 받아올 것
        } else if (currencyPair.equals("USDT")) {
            Multiple = 1 / 1300D;       //추후 원달러 환율 API를 통해 값 받아올 것
        }
        return totalTokenAmount * currentPrice * Multiple;
    }

}
