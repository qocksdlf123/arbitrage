package com.arbitrage.domain.upbit.service;

import com.arbitrage.domain.upbit.dao.UpbitPairRepository;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.upbit.UpbitExchange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Currency;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitPairService {
    Exchange upbit = ExchangeFactory.INSTANCE.createExchange(UpbitExchange.class);
    MarketDataService marketDataService = upbit.getMarketDataService();
    private final UpbitPairRepository upbitPairRepository;

    public void saveUpbitPair() {      // 1회성 pair 쌍 저장 메서드
        Exchange upbit = ExchangeFactory.INSTANCE.createExchange(UpbitExchange.class);
        List<Instrument> exchangeInstruments = upbit.getExchangeInstruments();
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
    public void addUpbitTokenName(){
        List<UpbitPair> upbitPairs = upbitPairRepository.findAll();
        upbitPairs.forEach((UpbitPair upbitPair) -> {
            // 구현 예정
        });
    }

    public Double currentPrice(CurrencyPair currencyPair) throws IOException {

        Ticker ticker = marketDataService.getTicker(currencyPair);
        return
    }


}
