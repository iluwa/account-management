package com.iluwa.accountmanagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RubConverter implements MoneyConverter {

    private static final BigDecimal RUB_MULTIPLIER = BigDecimal.valueOf(100);

    @Override
    public Long toLong(BigDecimal value) {
        return value.multiply(RUB_MULTIPLIER)
                .longValue();
    }

    @Override
    public BigDecimal toDecimal(Long value) {
        return BigDecimal.valueOf(value)
                .divide(RUB_MULTIPLIER, 2, RoundingMode.UP);
    }
}
