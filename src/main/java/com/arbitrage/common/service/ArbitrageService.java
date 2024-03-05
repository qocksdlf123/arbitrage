package com.arbitrage.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArbitrageService {
    private final RestTemplate restTemplate;

    public String getDAW(Currency currency) {
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

        HttpEntity<String> request = new HttpEntity<>(graphqlEndpoint, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(graphqlEndpoint, HttpMethod.POST, request, String.class);


        List<String> strings = responseEntity.getHeaders().get("Set-Cookie");

        String csrf_token = strings.get(0);
        Integer indexOfEqual = csrf_token.indexOf("=");
        Integer indexOfSemicolon = csrf_token.indexOf(";");
        String refeshToken = csrf_token.substring(indexOfEqual + 1,indexOfSemicolon);
        return refeshToken;





    }

    public String getRefreshToken() {
        String refreshTokenURL = "https://theddari.com/api/auth/session";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(refreshTokenURL, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(refreshTokenURL, HttpMethod.GET, request, String.class);


        List<String> strings = responseEntity.getHeaders().get("Set-Cookie");

        String csrf_token = strings.get(0);
        Integer indexOfEqual = csrf_token.indexOf("=");
        Integer indexOfSemicolon = csrf_token.indexOf(";");
        String refeshToken = csrf_token.substring(indexOfEqual + 1,indexOfSemicolon);
        return refeshToken;
    }
}
