package com.arbitrage.common.service;

import com.arbitrage.domain.bithumb.dao.BithumbPairRepository;
import com.arbitrage.domain.bithumb.domain.BithumbPair;
import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.dao.HuobiPairRepository;
import com.arbitrage.domain.huobi.domain.HuobiPair;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BithumbHuobiServcie {
    private final BithumbPairService bithumbPairService;

    private final BithumbPairRepository bithumbPairRepository;

    private final HuobiPairRepository huobiPairRepository;

    private final HuobiPairService huobiPairService;

    Exchange Huobi = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class);
    MarketDataService huobiMarketDataService = Huobi.getMarketDataService();

    public void compare() {
        List<BithumbPair> bithumbPairList = bithumbPairRepository.findAll();
        List<HuobiPair> huobiPairList = huobiPairRepository.findAll();

        HashSet<String> pairList = new HashSet<>();
        for (int i = 0; i < bithumbPairList.size(); i++) {
            BithumbPair bithumbPair = bithumbPairList.get(i);
            for (int j = 0; j < huobiPairList.size(); j++) {
                HuobiPair huobiPair = huobiPairList.get(j);
                if (bithumbPair.getTokenSymbol().equals(huobiPair.getTokenSymbol())
//                        && bithumbPair.getCurrency().equals("KRW")
                ) {

                    pairList.add(bithumbPair.getTokenSymbol());
                }
            }
        }
        log.info("pairList : {} ", pairList.toString());
        Iterator<String> iterator = pairList.iterator();
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            log.info("symbol : {}", symbol);
        }
    }
    /*
     현재의 빗썸 가격을 가져와 HTX의 오더북 기준 15% 내에 있는 물량 확인
     */
    @Scheduled(cron = "0 */5 * * * * ")
    public void getBFCQuantity() throws IOException {
        CurrencyPair currencyPair = new CurrencyPair("BFC/USDT");
        Double currentPrice = bithumbPairService.getCurrentPrice(currencyPair);
        Double totalTokenAmount = 0D;
        OrderBook orderBook = huobiMarketDataService.getOrderBook(currencyPair);
        log.info("currentPrice : {}", currentPrice);

        List<LimitOrder> asks = orderBook.getAsks();
        for (LimitOrder ask : asks) {
            log.info("ask : {}", ask);
            Double limitPrice = ask.getLimitPrice().doubleValue();
            if (currentPrice / limitPrice >= 1.06) {
                totalTokenAmount += ask.getOriginalAmount().doubleValue();
            }
        }

        String currency = currencyPair.getCounter().toString();

        Double multiple = 0D;
        log.info("currency : {}", currency);
        if (currency.equals("KRW")) {
            multiple = 1D;
        } else if (currency.equals("BTC")) {
            multiple = 90000000D; // 추후 비트코인 가격을 통해 값 받아올 것
        } else if (currency.equals("USDT")) {
            multiple = 1300D;       //추후 원달러 환율 API를 통해 값 받아올 것
        }
        log.info("totalTokenAmount : {}", totalTokenAmount);
        log.info("totalTokenAmount * currentPrice * Multiple : {}", totalTokenAmount * currentPrice * multiple);
    }
}
