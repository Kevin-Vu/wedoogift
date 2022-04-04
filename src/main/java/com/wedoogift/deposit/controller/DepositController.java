package com.wedoogift.deposit.controller;

import com.wedoogift.deposit.dto.DepositDto;
import com.wedoogift.deposit.exception.ChallengeBadRequestException;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;
import com.wedoogift.deposit.service.DepositService;
import com.wedoogift.deposit.utils.DepositUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Locale;

@OpenAPIDefinition(tags = @Tag(name = "Deposit API"))
@RestController
@RequestMapping("/api/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @Autowired
    private MessageSource msg;

    /**
     * Add a deposit
     *
     * @param depositDto : DepositDto
     *
     * @return : HttpStatus
     */
    @Operation(summary = "Add a deposit")
    @PostMapping
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<DepositDto> addDeposit(@RequestBody DepositDto depositDto)
            throws ChallengeBadRequestException, ChallengeNotFoundException, ChallengeUnauthorizedException {
        if(!DepositUtil.checkDeposit(depositDto))
            throw new ChallengeBadRequestException(
                    msg.getMessage("exception.bad.request.deposit", null, Locale.FRENCH), "exception.bad.request.deposit");
        return new ResponseEntity<>(this.depositService.addDeposit(depositDto), HttpStatus.OK);
    }

}
