package com.arbitrage.common.service;

import com.arbitrage.common.kakao.KakaoMSGService;
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

    private final KakaoMSGService kakaoMSGService;

    private final KimPremium kimPremium;
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

    @Scheduled(cron = "0 */30 * * * * ")
    public void getProfit() throws IOException {
        CurrencyPair currencyPair = new CurrencyPair("LBL/USDT");
        Double bithumbtPrice = bithumbPairService.getCurrentPrice(currencyPair);
        Double totalProfit = 0D;
        OrderBook orderBook = huobiMarketDataService.getOrderBook(currencyPair);
        Double kimP = kimPremium.getKimPremium();
        Double exchangeRate = kimPremium.getExchangeRate();

        log.info("currentPrice : {}", bithumbtPrice);

        List<LimitOrder> asks = orderBook.getAsks();
        for (LimitOrder ask : asks) {
            log.info("ask : {}", ask);
            Double limitPrice = ask.getLimitPrice().doubleValue();
            Double profit = bithumbtPrice - limitPrice * exchangeRate * kimP * 0.01;
            log.info("profit : {}",profit);
//            log.info("limitPrice : {}, exchangeRate : {}, kimP : {}",limitPrice, exchangeRate, kimP * 0.01);
//            log.info("currentPrice : {}, lek : {}", bithumbtPrice, limitPrice * exchangeRate * kimP * 0.01);
            if (profit >= 0 ) {
                totalProfit += profit * ask.getOriginalAmount().doubleValue();
                log.info("ask.getOriginalAmount().doubleValue() : {} ",ask.getOriginalAmount().doubleValue());
            }
        }

        String currency = currencyPair.getCounter().toString();

        log.info("currency : {}", currency);
        log.info("totalProfit : {}", totalProfit);
//        if(totalProfit>= 100000) {
//            String accessToken = kakaoMSGService.accessTokenReissue();
//            kakaoMSGService.sendMeMSG("LBL", "https://www.htx.com/trade/lbl_usdt?type=spot", accessToken);
//        }
    }
}
