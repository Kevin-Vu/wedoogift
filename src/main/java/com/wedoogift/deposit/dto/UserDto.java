package com.wedoogift.deposit.dto;

import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private BigDecimal balance;
    private String firstName;
    private String lastName;
    private String email;

}

