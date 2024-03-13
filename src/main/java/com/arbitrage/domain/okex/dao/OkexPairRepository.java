package com.arbitrage.domain.okex.dao;

import com.arbitrage.domain.okex.domain.OkexPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OkexPairRepository extends JpaRepository<OkexPair, Integer> {

}
