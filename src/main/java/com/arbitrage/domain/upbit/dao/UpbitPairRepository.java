package com.arbitrage.domain.upbit.dao;

import com.arbitrage.domain.upbit.domain.UpbitPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpbitPairRepository extends JpaRepository<UpbitPair, Integer> {

}
