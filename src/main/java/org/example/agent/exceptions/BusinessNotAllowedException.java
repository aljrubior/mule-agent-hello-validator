package org.example.agent.exceptions;

import com.mulesoft.agent.exception.ApplicationValidationException;

public class BusinessNotAllowedException extends ApplicationValidationException {

    public BusinessNotAllowedException(String message) {
        super(message);
    }
}