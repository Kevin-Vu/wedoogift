package com.wedoogift.deposit.controller;

import com.wedoogift.deposit.dto.UserDto;
import com.wedoogift.deposit.exception.ChallengeBadRequestException;
import com.wedoogift.deposit.exception.ChallengeNotFoundException;
import com.wedoogift.deposit.service.UserService;
import com.wedoogift.deposit.utils.UserUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Locale;

@OpenAPIDefinition(tags = @Tag(name = "User API"))
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a User
     *
     * @param userCreateDto : UserCreateDto
     *
     * @return : UserDto
     */
    @Operation(summary = "Create a new user")
    @PostMapping
    @Transactional
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userCreateDto) throws ChallengeBadRequestException {
        if(!UserUtil.checkCreateUserInput(userCreateDto))
            throw new ChallengeBadRequestException(
                    messageSource.getMessage("exception.bad.request.user", null, Locale.FRENCH), "exception.bad.request.user");
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.OK);
    }

    /**
     * Get a User
     *
     * @param id : user id
     *
     * @return : UserDto
     */
    @Operation(summary = "Get a user by its id")
    @GetMapping
    public ResponseEntity<UserDto> getUser(@RequestParam Integer id) throws ChallengeNotFoundException {
        return new ResponseEntity<>(userService.loadUserById(id), HttpStatus.OK);
    }

    /**
     * Delete a user by its id
     *
     * @param id : user id
     *
     * @return : HttpStatus
     */
    @Operation(summary = "Delete an user")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam Integer id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update a user
     *
     * @param userDto : UserDto
     *
     * @return : UserDto
     */
    @Operation(summary = "Update an user")
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) throws ChallengeBadRequestException, ChallengeNotFoundException {
        if(!UserUtil.checkUserInput(userDto))
            throw new ChallengeBadRequestException(
                    messageSource.getMessage("exception.bad.request.user", null, Locale.FRENCH), "exception.bad.request.user");
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

}
