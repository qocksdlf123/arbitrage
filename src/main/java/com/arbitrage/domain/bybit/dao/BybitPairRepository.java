package com.arbitrage.domain.bybit.dao;

import com.arbitrage.domain.bybit.domain.BybitPair;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BybitPairRepository extends JpaRepository<BybitPair, Integer> {

}
