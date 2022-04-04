package com.wedoogift.deposit.service;

import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.exception.ChallengeUnauthorizedException;

import java.math.BigDecimal;

public interface UserService {

    /**
     * Load a user by its id
     * @param userId : user id
     * @return : UserDto
     */
    UserDto loadUserById(Integer userId) throws ChallengeNotFoundException;

    /**
     * Create a user
     * @param userCreateDto : new user
     * @return : UserDto
     */
    UserDto createUser(UserDto userCreateDto);

    /**
     * Update a user
     * @param userDto : user with updated info
     * @return : UserDto
     * @throws ChallengeNotFoundException : user not found
     */
    UserDto updateUser(UserDto userDto) throws ChallengeNotFoundException;

    /**
     * Delete a user by its id
     * @param id : user id
     */
    void deleteUser(Integer id);

    /**
     * Compute the user's balance
     * Batch Purpose
     * @param user : user entity
     * @param typeEnum : deposit type
     * @return : user balance amount
     */
    BigDecimal computeBalance(UserEntity user, DepositPaymentTypeEnum typeEnum);

    /**
     * Update the user's balance
     * @param userId : user id
     * @param typeEnum : deposit type
     * @throws ChallengeNotFoundException : user not found
     */
    void updateBalance(Integer userId, DepositPaymentTypeEnum typeEnum) throws ChallengeNotFoundException, ChallengeUnauthorizedException;

}
