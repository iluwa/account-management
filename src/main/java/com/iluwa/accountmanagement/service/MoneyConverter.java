package com.iluwa.accountmanagement.service;

import java.math.BigDecimal;

public interface MoneyConverter {

    Long toLong(BigDecimal value);

    BigDecimal toDecimal(Long value);
}
