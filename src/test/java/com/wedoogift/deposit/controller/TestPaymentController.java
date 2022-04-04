package com.wedoogift.deposit.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wedoogift.deposit.configuration.BaseTest;
import com.wedoogift.deposit.dto.PaymentDto;
import com.wedoogift.deposit.entity.UserEntity;
import com.wedoogift.deposit.enumeration.DepositPaymentTypeEnum;
import com.wedoogift.deposit.repository.UserRepository;
import com.wedoogift.deposit.service.UserService;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:test.properties"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestPaymentController extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<PaymentDto> jsonPaymentDto;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String URL = "/api/payment";

    @Before
    public void before() {

        JacksonTester.initFields(this, JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build());
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test create payment
     *
     * @throws Exception
     */
    @Test
    public void testCreatePayment() throws Exception {

        // Given
        PaymentDto paymentDto = PaymentDto.builder()
                .amount(BigDecimal.valueOf(50))
                .object("REPAS COPIEUX")
                .receiver("DAROCO")
                .type(DepositPaymentTypeEnum.MEAL)
                .userId(2)
                .build();

        UserEntity user = this.userRepository.findById(2).orElse(null);
        BigDecimal userAmountBefore = user.getMealBalance().getAmount();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonPaymentDto.write(paymentDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Then
        user = this.userRepository.findById(2).orElse(null);
        BigDecimal userAmountAfter = user.getMealBalance().getAmount();;

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(0, userAmountBefore.subtract(BigDecimal.valueOf(50)).compareTo(userAmountAfter));

    }

    /**
     * Test create payment with an amount that is superior than the user's balance
     *
     * @throws Exception
     */
    @Test
    public void testCreatePaymentSuperiorUserBalance() throws Exception {

        // Given
        PaymentDto paymentDto = PaymentDto.builder()
                .amount(BigDecimal.valueOf(5000))
                .object("REPAS TROP COPIEUX")
                .receiver("DAROCO")
                .type(DepositPaymentTypeEnum.MEAL)
                .userId(2)
                .build();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonPaymentDto.write(paymentDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Then
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

    }

}
