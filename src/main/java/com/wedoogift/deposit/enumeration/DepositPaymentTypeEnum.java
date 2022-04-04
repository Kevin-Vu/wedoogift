package com.wedoogift.deposit.enumeration;

import lombok.Getter;

public enum DepositPaymentTypeEnum {

    GIFT("GIFT", "Gift"),
    MEAL("MEAL", "Meal");

    @Getter
    private final String code;
    @Getter
    private final String libelle;

    DepositPaymentTypeEnum(String code, String libelle)
    {
        this.code = code;
        this.libelle = libelle;
    }

}
