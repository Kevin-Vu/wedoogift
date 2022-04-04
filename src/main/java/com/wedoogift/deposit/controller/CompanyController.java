package com.wedoogift.deposit.controller;

import com.wedoogift.deposit.dto.CompanyDto;
import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.exception.ChallengeBadRequestException;
import com.wedoogift.deposit.service.CompanyService;
import com.wedoogift.deposit.service.UserService;
import com.wedoogift.deposit.utils.CompanyUtil;
import com.wedoogift.deposit.utils.UserUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@OpenAPIDefinition(tags = @Tag(name = "Company API"))
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a company
     *
     * @param companyDto : CompanyDto
     *
     * @return : UserDto
     */
    @Operation(summary = "Create a new company")
    @PostMapping
    public ResponseEntity<CompanyDto> createUser(@RequestBody CompanyDto companyDto) throws ChallengeBadRequestException {
        if(!CompanyUtil.checkCompanyCreate(companyDto))
            throw new ChallengeBadRequestException(
                    messageSource.getMessage("exception.bad.request.company", null, Locale.FRENCH), "exception.bad.request.company");
        return new ResponseEntity<>(this.companyService.createCompany(companyDto), HttpStatus.OK);
    }


}
