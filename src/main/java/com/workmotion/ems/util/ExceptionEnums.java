package com.workmotion.ems.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;


/**
 * a supplier of custom exceptions
 */
public enum ExceptionEnums implements Supplier<EMSException> {

    VALIDATION_EXCEPTION(new EMSException(HttpStatus.BAD_REQUEST, 400001,"Something went wrong, please contact system administrator.")),
    INVALID_INPUT_EXCEPTION(new EMSException(HttpStatus.BAD_REQUEST, 400002,"Invalid input data.")),

    EMPLOYEE_NOT_FOUND(new EMSException(HttpStatus.NOT_FOUND, 404001, "Not found.")),
    CONTRACT_NOT_FOUND(new EMSException(HttpStatus.NOT_FOUND, 404002, "Contract not found.")),

    UNHANDLED_EXCEPTION(new EMSException(HttpStatus.INTERNAL_SERVER_ERROR, 500001, "Unhandled exception has occured."));

    /// ====== ======///
    private EMSException e;

    /**
     * needed to put exceptions inside the enums
     * 
     * @param ex
     */
    private ExceptionEnums(EMSException ex) {
        this.e = ex;
    }

    public void message(String message) {
        e.errorMessage(message);
    }

    /**
     * gets the EMSException instance triggered by the enums
     * 
     * @param e
     * @return EMSException of defined enums
     */
    @Override
    public EMSException get() {
        return e;
    }

    // validate internal error codes
    static {
        final int errorCodeLength = 6;
        List<List<ExceptionEnums>> duplicateCodes = Arrays.stream(ExceptionEnums.values())
                .collect(Collectors.groupingBy(e -> e.get().getInternalCode())).values().stream()
                .filter(e -> e.size() > 1).collect(Collectors.toList());

        if (!duplicateCodes.isEmpty()) {
            throw new IllegalStateException("Duplicate error codes found: " + duplicateCodes);

        }

        for (ExceptionEnums e : ExceptionEnums.values()) {
            if (String.valueOf(e.get().getInternalCode()).length() != errorCodeLength) {
                throw new IllegalStateException(e + " -  Error code must have " + errorCodeLength + " digits");
            }
        }

        for (ExceptionEnums e : ExceptionEnums.values()) {
            if (!String.valueOf(e.get().getInternalCode()).substring(0, 3)
                    .equals(String.valueOf(e.get().getHttpCode().value()))) {
                throw new IllegalStateException(e + " - Error code prefix does not match http code");
            }
        }
    }
}
