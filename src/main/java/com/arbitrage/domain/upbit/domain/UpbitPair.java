package com.arbitrage.domain.upbit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpbitPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upbit_pair_id")
    Integer upbitPairId;
    @Column(name = "token_name")
    String tokenName;
    @Column(name = "token_symbol")
    String tokenSymbol;

    @Column(name ="currency")
    String currency;

}
