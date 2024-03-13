package com.arbitrage.domain.upbit.dto;

import lombok.Data;

@Data
public class UpbitCurrencyRequestDto {
    private String market;
    private Double trade_price;
    private Double trade_volume;
}
