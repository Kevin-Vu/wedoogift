package com.wedoogift.deposit.utils;

import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.entity.UserEntity;

public class ConvertorUtil {

    // Considering using mapstruct in further version
    private ConvertorUtil(){}

    /**
     * Convert a user entity to do
     * @param userEntity : UserEntity
     * @return : UserDto
     */
    public static UserDto userEntityToDto(UserEntity userEntity)
    {
        return UserDto.builder()
                .id(userEntity.getId())
                .balance(userEntity.getGiftBalance().getAmount())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstname())
                .lastName(userEntity.getLastname())
                .build();
    }

    /**
     * Convert a user create dto to entity
     * @param userCreateDto : UserDto
     * @return : UserEntity
     */
    public static UserEntity userCreateDtoToEntity(UserDto userCreateDto)
    {
        return UserEntity.builder()
                .email(userCreateDto.getEmail())
                .firstname(userCreateDto.getFirstName())
                .lastname(userCreateDto.getLastName())
                .build();
    }

}
