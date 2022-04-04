package com.wedoogift.deposit.service.impl;

import com.wedoogift.deposit.dto.DepositDto;
import com.wedoogift.deposit.entity.BalanceEntity;
import com.wedoogift.deposit.entity.CompanyEntity;
import com.wedoogift.deposit.entity.DepositEntity;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;
import com.wedoogift.deposit.repository.BalanceRepository;
import com.wedoogift.deposit.repository.CompanyRepository;
import com.wedoogift.deposit.repository.DepositRepository;
import com.wedoogift.deposit.repository.UserRepository;
import com.wedoogift.deposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Locale;


@Service
public class DepositServiceImpl implements DepositService {

    @Autowired
    private MessageSource msg;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    @Transactional
    public DepositDto addDeposit(DepositDto depositDto) throws ChallengeNotFoundException, ChallengeUnauthorizedException {
        UserEntity user = this.userRepository.findById(depositDto.getUserId()).orElse(null);
        CompanyEntity company = this.companyRepository.findById(depositDto.getCompanyId()).orElse(null);

        // Check if user and company exist
        if(user == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.user", new Object[]{depositDto.getUserId()}, Locale.FRENCH),
                    "exception.not.found.user");
        if(company == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.company", new Object[]{depositDto.getCompanyId()}, Locale.FRENCH), "" +
                    "exception.not.found.company");

        // Check and update company balance
        BalanceEntity companyBalance = company.getBalance();
        if(companyBalance.getAmount().compareTo(depositDto.getAmount()) < 0)
            throw new ChallengeUnauthorizedException(msg.getMessage("exception.unauthorized.deposit", null, Locale.FRENCH), "" +
                    "exception.unauthorized.deposit");
        companyBalance.setAmount(companyBalance.getAmount().subtract(depositDto.getAmount()));

        // Create deposit
        DepositEntity deposit = DepositEntity.builder()
                .amount(depositDto.getAmount())
                .beginDate(depositDto.getBeginDate())
                .company(company)
                .user(user)
                .remainingAmount(depositDto.getAmount())
                .type(depositDto.getType())
                .build();
        switch (deposit.getType())
        {
            case GIFT:
                deposit.setEndDate(deposit.getBeginDate().plusDays(365));
                // Update user's balance
                if(deposit.getBeginDate().isBefore(LocalDateTime.now()))
                {
                    BalanceEntity userBalance = user.getGiftBalance();
                    userBalance.setAmount(userBalance.getAmount().add(deposit.getAmount()));
                    this.userRepository.save(user);
                }
                break;

            case MEAL:
                boolean isNextYearLeap = Year.isLeap(deposit.getBeginDate().getYear() + 1L);
                if(isNextYearLeap)
                    deposit.setEndDate(LocalDateTime.of(deposit.getBeginDate().getYear() + 1, 2, 29, 0 ,0));
                else
                    deposit.setEndDate(LocalDateTime.of(deposit.getBeginDate().getYear() + 1, 2, 28, 0 ,0));
                // Update user's balance
                if(deposit.getBeginDate().isBefore(LocalDateTime.now()))
                {
                    BalanceEntity userBalance = user.getMealBalance();
                    userBalance.setAmount(userBalance.getAmount().add(deposit.getAmount()));
                    this.userRepository.save(user);
                }
                break;

            default:
                throw new ChallengeUnauthorizedException(msg.getMessage("exception.unauthorized.deposit", null, Locale.FRENCH), "" +
                        "exception.unauthorized.deposit");
        }

        // Check that end date is not in the past
        if(deposit.getEndDate().isBefore(LocalDateTime.now()))
            throw new ChallengeUnauthorizedException(msg.getMessage("exception.unauthorized.deposit", null, Locale.FRENCH), "" +
                    "exception.unauthorized.deposit");

        // Save
        this.balanceRepository.save(companyBalance);
        deposit = this.depositRepository.save(deposit);

        depositDto.setId(deposit.getId());
        depositDto.setEndDate(deposit.getEndDate());
        return depositDto;
    }

}
