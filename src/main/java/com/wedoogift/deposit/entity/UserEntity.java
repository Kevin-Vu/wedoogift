package com.wedoogift.deposit.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_challenge")
public class UserEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "user_generator", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @Column(name = "usr_id")
    private Integer id;

    @Column(name = "usr_firstname", nullable = false)
    private String firstname;

    @Column(name = "usr_lastname", nullable = false)
    private String lastname;

    @Column(name = "usr_email", nullable = false, unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "usr_ptr_gift_balance_id")
    private BalanceEntity giftBalance;

    @OneToOne
    @JoinColumn(name = "usr_ptr_meal_balance_id")
    private BalanceEntity mealBalance;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<DepositEntity> deposits;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentEntity> payments;

}
