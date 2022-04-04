package com.wedoogift.deposit.controller;

import com.wedoogift.deposit.dto.PaymentDto;
import com.wedoogift.deposit.exception.ChallengeBadRequestException;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;
import com.wedoogift.deposit.service.PaymentService;
import com.wedoogift.deposit.utils.PaymentUtil;
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

@OpenAPIDefinition(tags = @Tag(name = "Payment API"))
@RestController
@RequestMapping("/api/payment")
public class PaymentController {


    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MessageSource msg;

    /**
     * Add a payment
     *
     * @param paymentDto : PaymentDto
     *
     * @return : PaymentDto
     */
    @Operation(summary = "Add a payment")
    @PostMapping
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<PaymentDto> addPayment(@RequestBody PaymentDto paymentDto)
            throws ChallengeBadRequestException, ChallengeNotFoundException, ChallengeUnauthorizedException {
        if(!PaymentUtil.checkPayment(paymentDto))
            throw new ChallengeBadRequestException(
                    msg.getMessage("exception.bad.request.payment", null, Locale.FRENCH), "exception.bad.request.payment");
        return new ResponseEntity<>(this.paymentService.addPayment(paymentDto), HttpStatus.OK);
    }


}
