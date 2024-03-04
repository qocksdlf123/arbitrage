package com.arbitrage.domain.binance.dao;

import com.arbitrage.domain.binance.domain.BinancePair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinancePairRepository extends JpaRepository<BinancePair, Integer> {

}
