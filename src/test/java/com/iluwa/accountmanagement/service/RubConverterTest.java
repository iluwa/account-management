package com.iluwa.accountmanagement.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RubConverterTest {

    private final MoneyConverter moneyConverter = new RubConverter();

    @Test
    void toDecimal() {
        BigDecimal res = moneyConverter.toDecimal(19045L);
        assertEquals(new BigDecimal("190.45"), res);
    }

    @Test
    void toLong() {
        Long res = moneyConverter.toLong(new BigDecimal("190.459"));
        assertEquals(19045, res);
    }
}