package com.iluwa.accountmanagement.jpa.entity;

import com.iluwa.accountmanagement.service.MoneyConverter;
import com.iluwa.accountmanagement.service.RubConverter;

public enum Currency {
    RUB(new RubConverter());

    private final MoneyConverter moneyConverter;

    Currency(MoneyConverter moneyConverter) {
        this.moneyConverter = moneyConverter;
    }

    public MoneyConverter getMoneyConverter() {
        return moneyConverter;
    }
}
