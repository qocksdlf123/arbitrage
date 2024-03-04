package com.arbitrage.domain.bithumb.domain;

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
public class BithumbPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bithumb_pair_id")
    Integer bithumbairId;
    @Column(name = "token_name")
    String tokenName;
    @Column(name = "token_symbol")
    String tokenSymbol;

    @Column(name ="currency")
    String currency;

}
