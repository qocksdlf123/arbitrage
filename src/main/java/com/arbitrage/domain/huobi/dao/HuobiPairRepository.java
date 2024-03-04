package com.arbitrage.domain.huobi.dao;

import com.arbitrage.domain.huobi.domain.HuobiPair;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuobiPairRepository extends JpaRepository<HuobiPair, Integer> {

}
