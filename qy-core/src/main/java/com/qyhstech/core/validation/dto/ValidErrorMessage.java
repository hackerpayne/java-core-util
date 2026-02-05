package com.qyhstech.core.validation.dto;

import lombok.Data;

/**
 *
 */
@Data
public class ValidErrorMessage {

    private String propertyPath;

    private String message;

    public ValidErrorMessage() {

    }

    public ValidErrorMessage(String propertyPath, String message) {
        this.propertyPath = propertyPath;
        this.message = message;
    }
}
