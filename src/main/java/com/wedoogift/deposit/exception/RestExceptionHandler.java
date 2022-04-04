package com.wedoogift.deposit.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Get a message from a property file given a key and a language
     *
     * @param messageKey Message key
     * @param language   Message language
     * @return Localized message
     * @throws ChallengeNotFoundException Exception thrown if the message key is not found in the associated property file
     */
    private String getMessageInLanguage(String messageKey, String language) throws ChallengeNotFoundException {
        Locale locale = Locale.forLanguageTag(language);
        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (NoSuchMessageException e) {
            throw new ChallengeNotFoundException(messageSource.getMessage("exception.not.found.message", null, locale), "exception.not.found.message");
        }
    }

    /**
     * Get a map containing, for each supported language, the localized associated message given a messageKey
     *
     * @param messageKey Key of the message to get in property files
     * @return LocalizedMessageMap
     */
    private Map<String, String> getLocalizedMessageMap(String messageKey) {

        Map<String, String> localizedMessageMap = new HashMap<>();
        String message;

        for (String language : Arrays.asList("fr", "en")) {
            try {
                message = getMessageInLanguage(messageKey, language);
                localizedMessageMap.put(language, message);
            } catch (ChallengeNotFoundException e) {
                // If the message couldn't be loaded in a particular language, set the message to null for that language
                localizedMessageMap.put(language, null);
            }
        }

        return localizedMessageMap;
    }

    /**
     * Build the ResponseEntity sent to the client
     *
     * @param localizedMessageMap Response body containing a map, with the message to display in each supported language
     * @param httpStatus          Response status
     * @return ResponseEntity
     */
    private ResponseEntity<Object> buildResponseEntity(Map<String, String> localizedMessageMap, HttpStatus httpStatus) {
        return new ResponseEntity<>(localizedMessageMap, httpStatus);
    }

    /* ---------------------- NOT FOUND EXCEPTIONS ---------------------- */

    @ExceptionHandler(ChallengeNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(ChallengeNotFoundException ex) {
        return buildResponseEntity(getLocalizedMessageMap(ex.getKey()), HttpStatus.NOT_FOUND);
    }

    /* ----------------------- BAD REQUEST EXCEPTIONS ---------------------- */

    @ExceptionHandler(ChallengeBadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(ChallengeBadRequestException ex) {
        return buildResponseEntity(getLocalizedMessageMap(ex.getKey()), HttpStatus.BAD_REQUEST);
    }

    /* ----------------------- UNAUTHORIZED EXCEPTIONS ---------------------- */

    @ExceptionHandler(ChallengeUnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(ChallengeUnauthorizedException ex) {
        return buildResponseEntity(getLocalizedMessageMap(ex.getKey()), HttpStatus.UNAUTHORIZED);
    }
}