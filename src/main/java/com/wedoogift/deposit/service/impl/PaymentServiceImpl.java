package com.wedoogift.deposit.service.impl;

import com.wedoogift.deposit.dto.PaymentDto;
import com.wedoogift.deposit.entity.DepositEntity;
import com.wedoogift.deposit.entity.PaymentEntity;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;
import com.wedoogift.deposit.repository.DepositRepository;
import com.wedoogift.deposit.repository.PaymentRepository;
import com.wedoogift.deposit.repository.UserRepository;
import com.wedoogift.deposit.service.PaymentService;
import com.wedoogift.deposit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private MessageSource msg;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public PaymentDto addPayment(PaymentDto paymentDto) throws ChallengeNotFoundException, ChallengeUnauthorizedException {
        UserEntity user = this.userRepository.findById(paymentDto.getUserId()).orElse(null);

        // Check if user exists
        if(user == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.user", new Object[]{paymentDto.getUserId()}, Locale.FRENCH),
                    "exception.not.found.user");

        // Can't pay if the balance amount is not enough
        boolean authorizedPayment = true;
        switch (paymentDto.getType()) {
            case MEAL:
                if(user.getMealBalance().getAmount().subtract(paymentDto.getAmount()).compareTo(BigDecimal.ZERO) < 0)
                    authorizedPayment = false;
                break;
            case GIFT:
                if(user.getGiftBalance().getAmount().subtract(paymentDto.getAmount()).compareTo(BigDecimal.ZERO) < 0)
                    authorizedPayment = false;
                break;

            default:
                authorizedPayment = false;
        }

        if(!authorizedPayment)
            throw new ChallengeUnauthorizedException(msg.getMessage("exception.unauthorized.payment", null, Locale.FRENCH), "" +
                    "exception.unauthorized.payment");

        // Get all deposit
        // Considering putting this portion of code in a method inside UserEntity
        List<DepositEntity> userDeposits = user.getDeposits().stream()
                .filter(d -> d.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0 &&
                        d.getBeginDate().isBefore(LocalDateTime.now()) && d.getEndDate().isAfter(LocalDateTime.now())
                        && d.getType().equals(paymentDto.getType()))
                .sorted(Comparator.comparing(DepositEntity::getBeginDate).reversed())
                .collect(Collectors.toList());

        BigDecimal remainingAmount = paymentDto.getAmount();

        // Decrease the remaining amount for all the user deposits
        for(DepositEntity deposit : userDeposits)
        {
            if(deposit.getRemainingAmount().compareTo(remainingAmount) > 0)
            {
                deposit.setRemainingAmount(deposit.getRemainingAmount().subtract(remainingAmount));
                break;
            }
            else
            {
                remainingAmount = remainingAmount.subtract(deposit.getRemainingAmount());
                deposit.setRemainingAmount(BigDecimal.ZERO);
            }
        }

        // Build payment
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .amount(paymentDto.getAmount())
                .receiver(paymentDto.getReceiver())
                .object(paymentDto.getObject())
                .user(user)
                .build();

        // Save
        this.depositRepository.saveAll(userDeposits);
        paymentEntity = this.paymentRepository.save(paymentEntity);

        // Update user's balance
        this.userService.updateBalance(paymentDto.getUserId(), paymentDto.getType());

        paymentDto.setId(paymentEntity.getId());
        return paymentDto;
    }


}
