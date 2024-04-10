package com.arbitrage.domain;

import com.arbitrage.common.service.ThettariService;
import com.arbitrage.domain.bithumb.service.BithumbPairService;
import com.arbitrage.domain.huobi.service.HuobiPairService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bithumb.BithumbExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Slf4j
public class ArbitrageApplicationTests {

    @Mock
    private HuobiPairService huobiPairService;

    @Mock
    private BithumbPairService bithumbPairService;

    Exchange bithumb = ExchangeFactory.INSTANCE.createExchange(BithumbExchange.class);

    @InjectMocks
    private ThettariService thettariService;

    @BeforeEach
    void setUp() {

    }


    @Test
    void testGetOrderbookVolumeForZBC_USDT() throws IOException {
        when(huobiPairService.getOrderbookVolume(new CurrencyPair("ZBC/USDT"), true, 3D)).thenReturn(1234.56);

        Double orderbookVolume = huobiPairService.getOrderbookVolume(new CurrencyPair("ZBC/USDT"), true, 3D);

        assertNotNull(orderbookVolume);
        assertEquals(1234.56, orderbookVolume);
        verify(huobiPairService).getOrderbookVolume(new CurrencyPair("ZBC/USDT"), true, 3D);
    }

    @Test
    void testGetDWStatusFromBithumb() {
        when(bithumbPairService.getDWStatus("Eth")).thenReturn(new Integer[]{1, 0});

        Integer[] status = bithumbPairService.getDWStatus("Eth");

        assertNotNull(status);
        assertArrayEquals(new Integer[]{1, 0}, status);
        verify(bithumbPairService).getDWStatus("Eth");
    }

    @Test
    void getBithumbInstruments() {
        List<Instrument> exchangeInstruments = bithumb.getExchangeInstruments();
        exchangeInstruments.forEach((i) -> {
            log.info("Bithumb Instrumnets : {}", i);
        });
    }


}
