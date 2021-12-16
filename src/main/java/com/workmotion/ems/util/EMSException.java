package com.workmotion.ems.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.google.common.base.MoreObjects;

import lombok.Getter;

@Getter
public class EMSException extends Exception {
    private static final long serialVersionUID = 525221636121142550L;
    
    private final HttpStatus httpCode;
    private final int internalCode;
    private final List<String> errors;
    private String errorMessage;

    public EMSException(HttpStatus httpCode, int internalCode, String message) {
        this(httpCode, internalCode, message, new ArrayList<>());
    }

    public EMSException(HttpStatus httpCode, int internalCode, String message, List<String> errors) {
        super();
        this.httpCode = httpCode;
        this.internalCode = internalCode;
        this.errorMessage = message;
        this.errors = errors;
    }

    public EMSException errorMessage(String message) {
        this.errorMessage = message;
        return this;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("httpCode", httpCode)
                .add("internalCode", internalCode)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
