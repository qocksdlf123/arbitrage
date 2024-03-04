package com.arbitrage.domain.gateio.domain;

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
public class GateioPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gateio_pair_id")
    Integer gateioPairId;
    @Column(name = "token_name")
    String tokenName;
    @Column(name = "token_symbol")
    String tokenSymbol;

    @Column(name ="currency")
    String currency;

}
