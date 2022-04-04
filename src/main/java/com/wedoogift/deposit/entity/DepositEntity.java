package com.wedoogift.deposit.entity;

import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deposit")
public class DepositEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "deposit_generator", sequenceName = "deposit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_generator")
    @Column(name = "dps_id")
    private Integer id;

    @Column(name = "dps_amount")
    private BigDecimal amount;

    @Column(name = "dps_remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "dps_begin_date")
    private LocalDateTime beginDate;

    @Column(name = "dps_end_date")
    private LocalDateTime endDate;

    @Column(name = "dps_type")
    @Enumerated(value = EnumType.STRING)
    private DepositPaymentTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dps_ptr_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dps_ptr_company_id")
    private CompanyEntity company;

}
