package com.arbitrage.domain.exchagepair.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangePair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_pair_id")
    Integer exchangePairId;

    @Column(name = "exchange_name1")
    String exchangeName1;

    @Column(name = "exchange_name2")
    String exchangeName2;

    @Column(name = "token_symbol")
    String tokenSymbol;

    @Column(name = "token_name")
    String tokenName;

    String currency;


}
