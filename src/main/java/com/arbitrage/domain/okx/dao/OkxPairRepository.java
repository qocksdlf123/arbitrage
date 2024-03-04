package com.arbitrage.domain.okx.dao;

import com.arbitrage.domain.okx.domain.OkxPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OkxPairRepository extends JpaRepository<OkxPair, Integer> {

}
