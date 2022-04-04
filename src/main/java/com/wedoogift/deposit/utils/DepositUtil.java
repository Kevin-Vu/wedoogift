package com.wedoogift.deposit.utils;

import com.wedoogift.deposit.dto.DepositDto;

public class DepositUtil {

    private DepositUtil(){}

    public static boolean checkDeposit(DepositDto depositDto)
    {
        return depositDto.getAmount() != null && depositDto.getBeginDate() != null &&
                depositDto.getCompanyId() != null && depositDto.getUserId() != null;
    }

}
