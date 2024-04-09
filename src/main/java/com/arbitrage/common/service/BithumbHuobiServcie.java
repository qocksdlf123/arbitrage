package com.arbitrage.common.service;

import com.arbitrage.domain.bithumb.dao.BithumbPairRepository;
import com.arbitrage.domain.bithumb.domain.BithumbPair;
import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.dao.HuobiPairRepository;
import com.arbitrage.domain.huobi.domain.HuobiPair;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BithumbHuobiServcie {
    private final BithumbPairService bithumbPairService;

    private final BithumbPairRepository bithumbPairRepository;

    private final HuobiPairRepository huobiPairRepository;

    private final HuobiPairService huobiPairService;

    public void compare(){
        List<BithumbPair> bithumbPairList = bithumbPairRepository.findAll();
        List<HuobiPair> huobiPairList = huobiPairRepository.findAll();

        HashSet<String> pairList = new HashSet<>();
        for (int i = 0; i < bithumbPairList.size(); i++) {
            BithumbPair bithumbPair = bithumbPairList.get(i);
            for (int j = 0; j <huobiPairList.size(); j++) {
                HuobiPair huobiPair = huobiPairList.get(j);
                if(bithumbPair.getTokenSymbol().equals(huobiPair.getTokenSymbol())
//                        && bithumbPair.getCurrency().equals("KRW")
                ){

                    pairList.add(bithumbPair.getTokenSymbol());
                }
            }
        }
        log.info("pairList : {} ",pairList.toString());
        Iterator<String> iterator = pairList.iterator();
        while (iterator.hasNext()){
            String symbol = iterator.next();
            log.info("symbol : {}",symbol);
        }
    }
}
