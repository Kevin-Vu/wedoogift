package com.wedoogift.deposit.service.impl;

import com.wedoogift.deposit.dto.CompanyDto;
import com.wedoogift.deposit.entity.BalanceEntity;
import com.wedoogift.deposit.entity.CompanyEntity;
import com.wedoogift.deposit.repository.BalanceRepository;
import com.wedoogift.deposit.repository.CompanyRepository;
import com.wedoogift.deposit.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class CompanyServiceImpl implements CompanyService {


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto)
    {
        // Create balance
        BalanceEntity balance = BalanceEntity.builder()
                .amount(companyDto.getBalance())
                .code(companyDto.getCorporateName())
                .build();
        balance = this.balanceRepository.save(balance);

        // Create company
        CompanyEntity company = CompanyEntity.builder()
                .balance(balance)
                .corporateName(companyDto.getCorporateName())
                .publicName(companyDto.getPublicName())
                .siren(companyDto.getSiren())
                .siret(companyDto.getSiret())
                .build();
        company = this.companyRepository.save(company);

        // Company id
        companyDto.setId(company.getId());
        return companyDto;
    }

}
