package com.wedoogift.deposit.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private Integer id;
    private String publicName;      // denomination sociale
    private String corporateName;   // raison sociale
    private String siren;
    private String siret;
    private BigDecimal balance;

}
