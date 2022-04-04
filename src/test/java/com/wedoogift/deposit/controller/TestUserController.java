package com.wedoogift.deposit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedoogift.deposit.configuration.BaseTest;
import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.factory.UserFactoryUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:test.properties"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestUserController extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<UserDto> jsonUserDto;

    private static final String URL = "/api/user";

    @Before
    public void before() {

        JacksonTester.initFields(this, new ObjectMapper());
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test get user
     *
     * @throws Exception
     */
    @Test
    public void testGetUser() throws Exception {

        // Given
        HttpHeaders params = new HttpHeaders();
        params.add("id", "1");

        // When
        MockHttpServletResponse response = mockMvc.perform(
                get(URL).params(params)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        UserDto userDto = jsonUserDto.parse(response.getContentAsString()).getObject();

        // Then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals("aurore.melie@gmail.com", userDto.getEmail());

    }

    /**
     * Test get bad user
     *
     * @throws Exception
     */
    @Test
    public void testGetBadUser() throws Exception {

        // Given
        HttpHeaders params = new HttpHeaders();
        params.add("id", "3");

        // When
        MockHttpServletResponse response = mockMvc.perform(
                get(URL).params(params)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Then
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

    }

    /**
     * Test delete user
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testDeleteUser() throws Exception {

        // Given
        HttpHeaders params = new HttpHeaders();
        params.add("id", "1");

        // When
        MockHttpServletResponse response = mockMvc.perform(
                delete(URL).params(params)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    /**
     * Test create user with "admin" authority
     *
     * @throws Exception
     */
    @Test
    public void testCreateUser() throws Exception {

        // Given
        UserDto createUserDto = UserFactoryUtils.generateUserDto();

        // When
        MockHttpServletResponse response = mockMvc.perform(
                post(URL).accept(MediaType.ALL).content(jsonUserDto.write(createUserDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        UserDto userDto = jsonUserDto.parse(response.getContentAsString()).getObject();

        // Then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(createUserDto.getEmail(), userDto.getEmail());

    }

}