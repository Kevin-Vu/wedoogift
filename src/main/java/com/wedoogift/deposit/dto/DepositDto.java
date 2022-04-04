package com.wedoogift.deposit.dto;

import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositDto {

    private Integer id;
    private BigDecimal amount;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private Integer companyId;
    private Integer userId;
    private DepositPaymentTypeEnum type;

}
