package com.arbitrage.domain.bithumb.dao;

import com.arbitrage.domain.bithumb.domain.BithumbPair;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BithumbPairRepository extends JpaRepository<BithumbPair, Integer> {

}
