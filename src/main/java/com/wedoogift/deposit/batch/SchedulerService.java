package com.wedoogift.deposit.batch;

import com.wedoogift.deposit.entity.BalanceEntity;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.repository.BalanceRepository;
import com.wedoogift.deposit.repository.UserRepository;
import com.wedoogift.deposit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserService userService;

    /**
     * Update all the balances every day at midnight
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateAllBalance()
    {
        List<UserEntity> userEntityList = this.userRepository.findAll();

        List<BalanceEntity> balanceEntityList = new ArrayList<>();
        // Compute remaining balance
        for(UserEntity user : userEntityList)
        {
            BigDecimal remainingGiftAmoung = this.userService.computeBalance(user, DepositPaymentTypeEnum.GIFT);
            BigDecimal remainingMealAmoung = this.userService.computeBalance(user, DepositPaymentTypeEnum.MEAL);

            BalanceEntity giftBalance = user.getGiftBalance();
            BalanceEntity mealBalance = user.getMealBalance();

            giftBalance.setAmount(remainingGiftAmoung);
            mealBalance.setAmount(remainingMealAmoung);

            balanceEntityList.add(giftBalance);
            balanceEntityList.add(mealBalance);

        }

        // Save
        this.balanceRepository.saveAll(balanceEntityList);
    }


}
