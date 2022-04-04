package com.wedoogift.deposit.utils;

import com.wedoogift.deposit.dto.CompanyDto;

public class CompanyUtil {

    private CompanyUtil(){}

    public static boolean checkCompanyCreate(CompanyDto companyDto)
    {
        return companyDto != null && companyDto.getCorporateName() != null &&
                companyDto.getPublicName() != null && companyDto.getSiren() != null && companyDto.getSiret() != null;
    }

}
