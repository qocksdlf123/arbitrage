package com.arbitrage.domain.okx.domain;

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
public class OkxPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "okx_pair_id")
    Integer okxPairId;
    @Column(name = "token_name")
    String tokenName;
    @Column(name = "token_symbol")
    String tokenSymbol;

    @Column(name ="currency")
    String currency;

}
