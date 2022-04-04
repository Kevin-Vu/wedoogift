package com.wedoogift.deposit.service;

import com.wedoogift.deposit.dto.DepositDto;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;

public interface DepositService {


    /**
     * Add a MEAL or Gift deposit from a company to a user
     * @param depositDto : DepositDto
     * @return : DepositDto
     */
    DepositDto addDeposit(DepositDto depositDto) throws ChallengeNotFoundException, ChallengeUnauthorizedException;

}
