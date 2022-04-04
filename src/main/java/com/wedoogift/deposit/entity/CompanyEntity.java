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
@Table(name = "company")
public class CompanyEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "company_generator", sequenceName = "company_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_generator")
    @Column(name = "cpn_id")
    private Integer id;

    @Column(name = "cpn_public_name")
    private String publicName; // denomination sociale

    @Column(name = "cpn_corporate_name")
    private String corporateName; // raison sociale

    @Column(name = "cpn_siren")
    private String siren;

    @Column(name = "cpn_siret")
    private String siret;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<DepositEntity> deposits;

    @OneToOne
    @JoinColumn(name = "cpn_ptr_balance_id")
    private BalanceEntity balance;

}
