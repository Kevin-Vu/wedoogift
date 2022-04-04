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
@Table(name = "payment")
public class PaymentEntity implements Serializable {


    @Id
    @SequenceGenerator(name = "payment_generator", sequenceName = "payment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_generator")
    @Column(name = "pay_id")
    private Integer id;

    @Column(name = "pay_amount")
    private BigDecimal amount;

    @Column(name = "pay_receiver")
    private String receiver;

    @Column(name = "pay_object")
    private String object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_ptr_user_id")
    private UserEntity user;


}
