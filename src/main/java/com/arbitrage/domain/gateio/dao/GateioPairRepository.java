package com.arbitrage.domain.gateio.dao;

import com.arbitrage.domain.gateio.domain.GateioPair;
import com.arbitrage.domain.upbit.domain.UpbitPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GateioPairRepository extends JpaRepository<GateioPair, Integer> {

}
