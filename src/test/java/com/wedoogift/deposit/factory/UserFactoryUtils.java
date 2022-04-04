package com.wedoogift.deposit.factory;

import com.wedoogift.deposit.dto.UserDto;
import org.apache.commons.lang3.RandomStringUtils;

public class UserFactoryUtils {

    private UserFactoryUtils(){}

    public static UserDto generateUserDto(){
        return UserDto.builder()
                    .firstName(RandomStringUtils.randomAlphabetic(20))
                    .lastName(RandomStringUtils.randomAlphabetic(20))
                    .email(RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" +
                            RandomStringUtils.randomAlphabetic(5).toLowerCase() + ".fr")
                    .build();
    }

}
