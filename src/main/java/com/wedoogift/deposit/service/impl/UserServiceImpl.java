package com.wedoogift.deposit.service.impl;

import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.entity.BalanceEntity;
import com.wedoogift.deposit.entity.DepositEntity;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;
import com.wedoogift.deposit.repository.BalanceRepository;
import com.wedoogift.deposit.repository.UserRepository;
import com.wedoogift.deposit.service.UserService;
import com.wedoogift.deposit.utils.ConvertorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MessageSource msg;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto loadUserById(Integer id) throws ChallengeNotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if(userEntity == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.user",
                    new Object[]{id}, Locale.FRENCH), "exception.not.found.user");
        return ConvertorUtil.userEntityToDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userCreateDto){
        UserEntity userEntity = ConvertorUtil.userCreateDtoToEntity(userCreateDto);
        BalanceEntity giftBalance = BalanceEntity.builder()
                .amount(BigDecimal.ZERO)
                .code(userCreateDto.getEmail() + "_" + DepositPaymentTypeEnum.GIFT)
                .build();
        BalanceEntity mealBalance = BalanceEntity.builder()
                .amount(BigDecimal.ZERO)
                .code(userCreateDto.getEmail() + "_" + DepositPaymentTypeEnum.MEAL)
                .build();
        mealBalance = this.balanceRepository.save(mealBalance);
        giftBalance = this.balanceRepository.save(giftBalance);
        userEntity.setGiftBalance(giftBalance);
        userEntity.setMealBalance(mealBalance);
        userEntity = userRepository.save(userEntity);
        return ConvertorUtil.userEntityToDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) throws ChallengeNotFoundException {
        UserEntity userEntity = this.userRepository.findById(userDto.getId()).orElse(null);
        if(userEntity == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.user",
                    new Object[]{userDto.getId()}, Locale.FRENCH), "exception.not.found.user");
        userEntity.setFirstname(userDto.getFirstName());
        userEntity.setLastname(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity = this.userRepository.save(userEntity);
        return ConvertorUtil.userEntityToDto(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id){
        this.userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBalance(Integer userId, DepositPaymentTypeEnum type) throws ChallengeNotFoundException, ChallengeUnauthorizedException {
        UserEntity user = this.userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new ChallengeNotFoundException(msg.getMessage("exception.not.found.user",
                    new Object[]{userId}, Locale.FRENCH), "exception.not.found.user");
        BigDecimal remainingAmoung = this.computeBalance(user, type);
        BalanceEntity balance;
        switch (type)
        {
            case MEAL:
                balance = user.getMealBalance();
                break;
            case GIFT:
                balance = user.getGiftBalance();
                break;
            default:
                throw new ChallengeUnauthorizedException(msg.getMessage("exception.unauthorized.balance", null, Locale.FRENCH), "" +
                        "exception.unauthorized.balance");
        }
        balance.setAmount(remainingAmoung);
        this.balanceRepository.save(balance);
    }

    @Override
    @Transactional
    public BigDecimal computeBalance(UserEntity user, DepositPaymentTypeEnum typeEnum)
    {
        LocalDateTime now = LocalDateTime.now();
        return user.getDeposits().stream()
                .filter(d -> d.getBeginDate().isBefore(now) && d.getEndDate().isAfter(now)
                        && d.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0 && d.getType().equals(typeEnum))
                .map(DepositEntity::getRemainingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
