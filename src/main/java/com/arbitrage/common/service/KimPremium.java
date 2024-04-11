package com.arbitrage.common.service;

import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KimPremium {

    private final BithumbPairService bithumbPairService;

    private final HuobiPairService huobiPairService;

    private final RestTemplate restTemplate;

    public Double getKimPremium() {
        Double exchangeRate = getExchangeRate();
        log.info("환율 : {},",exchangeRate);
        Double huobiBTCPrice = 0D;
        Double bithumbBTCPrice = 0D;
        try {
            huobiBTCPrice = huobiPairService.getCurrentPrice(new CurrencyPair("BTC/USDT")) * exchangeRate;
            bithumbBTCPrice = bithumbPairService.getCurrentPrice(new CurrencyPair("BTC/KRW"));
            log.info("huobiBTCPrice : {}, bithumbBTCPrice : {}", huobiBTCPrice, bithumbBTCPrice);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (bithumbBTCPrice / huobiBTCPrice - 1) * 100;


    }

//    public Double getExchangeRate() {
//        String apiKey = "RxhRuyEqj2RNluy3Xk0qm1IeWGUiRpMW";
//        String requestType = "AP01"; //AP01는 환율
//        String url = String.format("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=%s&searchdate=20240411&data=%s", apiKey, requestType);
//
//        String jsonString = restTemplate.getForObject(url, String.class);  //노드 JSON이 배열이기떄문에
//        log.info("jsonString : {}",jsonString);
//        ObjectMapper objectMapper = new ObjectMapper();                    //ObjectMapper를 만든 뒤
//        JsonNode jsonNode = null;
//        try {
//            jsonNode = objectMapper.readTree(jsonString);                 //JsonNode로 readTree를 통해 파싱
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        log.info("JsonNode : {}",jsonNode);
//        String exchangeRateString  = jsonNode
//                .get(22)
//                .get("deal_bas_r")
//                .toString()
//                .replace(",","")     //1,325.3 에서 , 제거
//                .replace("\"","");   //문자내열 내 쌍따음표 제거
//
//        double exchangeRateDouble = Double.parseDouble(exchangeRateString);    //1,325.3 에서 , 제거
//        return exchangeRateDouble;
//    }

    public Double getExchangeRate(){
        String url = "https://m.search.naver.com/p/csearch/content/qapirender.nhn?key=calculator&pkid=141&q=%ED%99%98%EC%9C%A8&where=m&u1=keb&u6=standardUnit&u7=0&u3=USD&u4=KRW&u8=down&u2=1";
        JsonNode jsonNode = restTemplate.getForObject(url,JsonNode.class);
        String exchangeRateString = jsonNode
                .get("country")
                .get(1)
                .get("value")
                .toString()
                .replace(",", "")
                .replace("\"", "");
        return Double.parseDouble(exchangeRateString);

    }



}
