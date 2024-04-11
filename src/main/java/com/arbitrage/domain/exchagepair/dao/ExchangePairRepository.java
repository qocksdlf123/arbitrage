package com.arbitrage.domain.exchagepair.dao;

import com.arbitrage.domain.exchagepair.entity.ExchangePair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangePairRepository extends JpaRepository<ExchangePair, Integer> {


}
