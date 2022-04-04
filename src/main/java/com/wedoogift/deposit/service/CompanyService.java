package com.wedoogift.deposit.service;

import com.wedoogift.deposit.dto.CompanyDto;

public interface CompanyService {

    /**
     * Create a new company
     * @param companyDto : CompanyDto
     * @return : CompanyDto
     */
    CompanyDto createCompany(CompanyDto companyDto);

}
