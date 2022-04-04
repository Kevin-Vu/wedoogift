package com.wedoogift.deposit.utils;

import com.wedoogift.deposit.dto.PaymentDto;

public class PaymentUtil {

    private PaymentUtil(){}

    public static boolean checkPayment(PaymentDto paymentDto)
    {
        return paymentDto != null && paymentDto.getAmount() != null && paymentDto.getUserId() != null;
    }

}
