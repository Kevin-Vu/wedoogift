package com.wedoogift.deposit.service;

import com.wedoogift.deposit.dto.PaymentDto;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;

public interface PaymentService {

    /**
     * Create a payment
     * @param paymentDto : Payment
     * @return : PaymentDto
     */
    PaymentDto addPayment(PaymentDto paymentDto) throws ChallengeNotFoundException, ChallengeUnauthorizedException;

}
