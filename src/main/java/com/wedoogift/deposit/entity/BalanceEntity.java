package com.wedoogift.deposit.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "balance")
public class BalanceEntity implements Serializable {


    @Id
    @SequenceGenerator(name = "balance_generator", sequenceName = "balance_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "balance_generator")
    @Column(name = "blc_id")
    private Integer id;

    @Column(name = "blc_code")
    private String code;

    @Column(name = "blc_amount")
    private BigDecimal amount;


}
