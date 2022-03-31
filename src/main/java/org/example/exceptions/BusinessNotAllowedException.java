package org.example.exceptions;

import com.mulesoft.agent.exception.ArtifactValidationException;

public class BusinessNotAllowedException extends ArtifactValidationException {

    public BusinessNotAllowedException(String message) {
        super(message);
    }
}