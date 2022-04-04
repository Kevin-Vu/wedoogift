package com.wedoogift.deposit.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wedoogift.deposit.configuration.BaseTest;
import com.wedoogift.deposit.dto.DepositDto;
import com.wedoogift.deposit.entity.CompanyEntity;
import com.wedoogift.deposit.entity.DepositEntity;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.repository.BalanceRepository;
import com.wedoogift.deposit.repository.CompanyRepository;
import com.wedoogift.deposit.repository.DepositRepository;
import com.wedoogift.deposit.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:test.properties"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestDepositController extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<DepositDto> jsonDepositDto;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepositRepository depositRepository;


    private static final String URL = "/api/deposit";

    @Before
    public void before() {

        JacksonTester.initFields(this, JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build());
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test meal deposit
     *
     * @throws Exception
     */
    @Test
    public void testCreateMealDeposit() throws Exception {

        // Given
        UserEntity user = this.userRepository.findById(1).orElse(null);
        BigDecimal userAmountBefore = user.getMealBalance().getAmount();

        CompanyEntity company = this.companyRepository.findById(1).orElse(null);
        BigDecimal companyAmountBefore = company.getBalance().getAmount();

        LocalDateTime now = LocalDateTime.now();

        DepositDto depositDto = DepositDto
                .builder()
                .userId(1)
                .beginDate(now.minusDays(2))
                .amount(BigDecimal.valueOf(20))
                .type(DepositPaymentTypeEnum.MEAL)
                .companyId(1)
                .build();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonDepositDto.write(depositDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        DepositDto depositResponse = jsonDepositDto.parse(response.getContentAsString()).getObject();


        // Then
        user = this.userRepository.findById(1).orElse(null);
        company = this.companyRepository.findById(1).orElse(null);
        DepositEntity deposit = this.depositRepository.findById(depositResponse.getId()).orElse(null);

        BigDecimal userAmountAfter = user.getMealBalance().getAmount();;
        BigDecimal companyAmountAfter = company.getBalance().getAmount();

        int nbOfDaysInFebruary = Year.isLeap(LocalDateTime.now().getYear() + 1) ? 29 : 28;

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(0, userAmountBefore.add(BigDecimal.valueOf(20)).compareTo(userAmountAfter));
        Assert.assertEquals(0, companyAmountBefore.subtract(BigDecimal.valueOf(20)).compareTo(companyAmountAfter));
        Assert.assertTrue(deposit.getEndDate().isEqual(LocalDateTime.of(now.getYear() + 1,2,nbOfDaysInFebruary,0,0)));

    }

    /**
     * Test gift deposit
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testCreateGiftDeposit() throws Exception {

        // Given
        UserEntity user = this.userRepository.findById(1).orElse(null);
        BigDecimal userAmountBefore = user.getGiftBalance().getAmount();

        CompanyEntity company = this.companyRepository.findById(1).orElse(null);
        BigDecimal companyAmountBefore = company.getBalance().getAmount();

        LocalDateTime now = LocalDateTime.now();

        DepositDto depositDto = DepositDto
                .builder()
                .userId(1)
                .beginDate(now.minusDays(2))
                .amount(BigDecimal.valueOf(20))
                .type(DepositPaymentTypeEnum.GIFT)
                .companyId(1)
                .build();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonDepositDto.write(depositDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        DepositDto depositResponse = jsonDepositDto.parse(response.getContentAsString()).getObject();


        // Then
        user = this.userRepository.findById(1).orElse(null);
        company = this.companyRepository.findById(1).orElse(null);
        DepositEntity deposit = this.depositRepository.findById(depositResponse.getId()).orElse(null);

        BigDecimal userAmountAfter = user.getGiftBalance().getAmount();
        BigDecimal companyAmountAfter = company.getBalance().getAmount();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(0, userAmountBefore.add(BigDecimal.valueOf(20)).compareTo(userAmountAfter));
        Assert.assertEquals(0, companyAmountBefore.subtract(BigDecimal.valueOf(20)).compareTo(companyAmountAfter));
        Assert.assertTrue(deposit.getEndDate().isEqual(now.minusDays(2).plusDays(365)));

    }

    /**
     * Test add deposit with an amount of money that is superior of the company's balance
     *
     * @throws Exception
     */
    @Test
    public void testCreateDepositAmountSuperiorAllowed() throws Exception {

        // Given
        DepositDto depositDto = DepositDto
                .builder()
                .userId(1)
                .beginDate(LocalDateTime.now().minusDays(2))
                .amount(BigDecimal.valueOf(10000000))
                .type(DepositPaymentTypeEnum.MEAL)
                .companyId(2)
                .build();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonDepositDto.write(depositDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();


        // Then
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

    }

    /**
     * Test add deposit with an end date in the past
     *
     * @throws Exception
     */
    @Test
    public void testCreateDepositEndDateInPast() throws Exception {

        // Given
        DepositDto depositDto = DepositDto
                .builder()
                .userId(1)
                .beginDate(LocalDateTime.of(1900, 1,1,0,0))
                .amount(BigDecimal.valueOf(50))
                .type(DepositPaymentTypeEnum.MEAL)
                .companyId(2)
                .build();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonDepositDto.write(depositDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();


        // Then
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

    }
}
