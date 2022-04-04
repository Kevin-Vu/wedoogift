package com.wedoogift.deposit.dto;

import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Integer id;
    private BigDecimal amount;
    private DepositPaymentTypeEnum type;
    private String receiver;
    private String object;
    private Integer userId;

}
