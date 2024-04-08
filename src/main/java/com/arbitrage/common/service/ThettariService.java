package com.arbitrage.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThettariService {
    private final RestTemplate restTemplate;

    public void getDAW() {
        String refreshToken = getRefreshToken();
        log.info("refreshToken : {}",refreshToken);
        String graphqlEndpoint = "https://api.thedddari.com/graphql";

        String graphqlQuery = "{\n" +
                "  getSymbolWallets(symbol: \"PLA\", exchanges: [\"gate\", \"huobi\", \"bitget\", \"mexc\"]) {\n" +
                "    s: symbol\n" +
                "    e: exchange\n" +
                "    d: deposit\n" +
                "    w: withdraw\n" +
                "    c: supportedChains\n" +
                "    cd: chainDeposits\n" +
                "    cw: chainWithdraws\n" +
                "    cc: chainDepositConfirms\n" +
                "    cf: chainWithdrawFees\n" +
                "    m: message\n" +
                "  }\n" +
                "}\n";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + refreshToken);
        HttpEntity<String> request = new HttpEntity<>(graphqlQuery, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(graphqlEndpoint, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();
        log.info("body : {}",body.toString());


    }

    public String getRefreshToken() {
        String refreshTokenURL = "https://theddari.com/api/auth/session";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(refreshTokenURL, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(refreshTokenURL, HttpMethod.GET, request, String.class);


        List<String> strings = responseEntity.getHeaders().get("Set-Cookie");

        String csrf_token = strings.get(0);
        log.info("csrf_token : {}",csrf_token);
        Integer indexOfEqual = csrf_token.indexOf("=");
        Integer indexOfSemicolon = csrf_token.indexOf(";");
        String refeshToken = csrf_token.substring(indexOfEqual + 1,indexOfSemicolon);
        return refeshToken;
    }
}
