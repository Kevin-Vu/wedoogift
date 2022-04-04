package com.wedoogift.deposit.utils;

import com.wedoogift.deposit.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class UserUtil {

    private UserUtil(){}

    /**
     * Check the content of a new user
     *
     * @param userCreateDto : CreateUserDto
     *
     * @return : true if input is valid
     */
    public static boolean checkCreateUserInput(UserDto userCreateDto){
        if(userCreateDto == null)
            return false;
        return StringUtils.isNoneBlank(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail()) &&
                EmailValidator.getInstance().isValid(userCreateDto.getEmail());
    }

    /**
     * Check the content of a user for update
     *
     * @param userDto : UserDto
     *
     * @return : true if input is valid
     */
    public static boolean checkUserInput(UserDto userDto){
        if(userDto == null || userDto.getId() == null || userDto.getBalance() == null)
            return false;
        return StringUtils.isNoneBlank(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail()) &&
                EmailValidator.getInstance().isValid(userDto.getEmail());
    }

}
