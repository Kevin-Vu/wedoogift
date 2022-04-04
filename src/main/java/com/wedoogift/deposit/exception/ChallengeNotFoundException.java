package com.wedoogift.deposit.exception;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChallengeNotFoundException extends Exception {

    private static final Logger log = LogManager.getLogger();

    @Getter
    private final String key;

    public ChallengeNotFoundException(String message, String key) {
        super(message);
        this.key = key;
        log.error("[BDD] - {}", message);
    }
}
