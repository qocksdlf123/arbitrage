package com.arbitrage.domain.upbit.service;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.upbit.UpbitExchange;
import java.util.List;
import java.util.Map;

@Slf4j
public class UpbitPrice {
    public static void main(String[] args) {
        try {
            // Upbit 거래소 객체 생성
            Exchange upbit = ExchangeFactory.INSTANCE.createExchange(UpbitExchange.class);
            MarketDataService marketDataService = upbit.getMarketDataService();
            AccountService accountService = upbit.getAccountService();
            ExchangeMetaData exchangeMetaData = upbit.getExchangeMetaData();
            ExchangeSpecification exchangeSpecification = upbit.getExchangeSpecification();


            log.info("exchangeSpecification : {}", exchangeSpecification);


            List<Instrument> exchangeInstruments = upbit.getExchangeInstruments();
            log.info("exchangeInstruments : {}",exchangeInstruments);
            log.info("exchangeInstruments 개수 : {}",exchangeInstruments.size());
            exchangeInstruments.forEach((Instrument i) -> {
                log.info("Instrument : {} {}",i.getBase(),i.getCounter() );
            });
            // 비트코인 대 KRW의 Ticker 조회
//            Ticker ticker = marketDataService.getTicker(CurrencyPair.BTC_KRW);
//
//            System.out.println("Ticker: " + ticker);
//            System.out.println("Last: " + ticker.getLast());
//            System.out.println("Bid: " + ticker.getBid());
//            System.out.println("Ask: " + ticker.getAsk());
//            System.out.println("Volume: " + ticker.getVolume());

            // BTC/KRW 오더북 조회
            OrderBook orderBook = marketDataService.getOrderBook(new CurrencyPair("AVAX/KRW"));
            log.info("orderBook : {}",orderBook.toString());
            Ticker ticker = marketDataService.getTicker(new CurrencyPair("AVAX/KRW"));
            log.info("ticker : {}",ticker.toString());
            Trades trades = marketDataService.getTrades(new CurrencyPair("AVAX/KRW"));
            log.info("trades : {}",trades.toString());
            Map<Instrument, InstrumentMetaData> instruments = exchangeMetaData.getInstruments();
            log.info("instruments : {}",instruments.toString());

//            FundingRates fundingRates = ma
//            log.info("ticker : {}",fundingRates);
            orderBook.getAsks().forEach(bid -> log.info("bid : {}",bid.toString()));
//            orderBook.getBids().forEach(bid -> System.out.println("Price: " + bid.getLimitPrice() + " Amount: " + bid.getOriginalAmount()));
//            List<LimitOrder> bids = orderBook.getBids();
//            // 매도 주문 출력
//            System.out.println("\nAsks:");
//            orderBook.getAsks().forEach(ask -> System.out.println("Price: " + ask.getLimitPrice() + " Amount: " + ask.getOriginalAmount()));

            //입출금 가능 여부
            log.info("accountService : {}",accountService);

//            marketDataService.getTickers("AVAX/KRW");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

