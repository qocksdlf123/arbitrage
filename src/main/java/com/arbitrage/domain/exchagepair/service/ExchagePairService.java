package com.arbitrage.domain.exchagepair.service;

import com.arbitrage.domain.exchagepair.dao.ExchangePairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchagePairService {

    private final ExchangePairRepository exchangePairRepository;


}
