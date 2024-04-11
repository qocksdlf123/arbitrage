package com.arbitrage.domain.huobi.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.service.account.AccountService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuobiAccountService {
    Exchange huobiExchange = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class);

    AccountService accountService = huobiExchange.getAccountService();

    public void getAccount(){
        AccountInfo accountInfo;
        try {
            accountInfo = accountService.getAccountInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Exchange huobiExchange = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class);

        AccountService accountService = huobiExchange.getAccountService();
    }

}
