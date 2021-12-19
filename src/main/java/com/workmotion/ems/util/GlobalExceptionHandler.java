package com.workmotion.ems.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.workmotion.ems.swagger.model.ResponseBase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String methodName = "handleMissingServletRequestParameter()";
        log.error(Constants.PARAMETER_2, methodName, ex.getMessage());
        String error = "Parameter " + ex.getParameterName() + " is missing";
        return buildResponseEntity(HttpStatus.BAD_REQUEST, headers, null, ex.getMessage(), Arrays.asList(error));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String methodName = "handleHttpRequestMethodNotSupported()";
        log.error(Constants.PARAMETER_2, methodName, ex.getMessage());
        return buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, headers, null, ex.getMessage(), Arrays.asList(""));
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String methodName = "handleNoHandlerFoundException()";
        log.error(Constants.PARAMETER_2, methodName, ex.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, null, null, ex.getMessage(), null);
    }

    @ExceptionHandler(value = MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedExceptionException(MethodNotAllowedException ex,
            WebRequest request) {
        return buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, null, null, ex.getMessage(), null);
    }

    @ExceptionHandler(value = Exception.class)
    @RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String methodName = "handleGenericException()";
        log.error(Constants.PARAMETER_3, methodName, ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, null, ex.getMessage(), null);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, HttpHeaders headers, Integer internalCode,
            String message, List<Object> errors) {
        ResponseBase response = new ResponseBase().success(false).message(message)
                .resultCode(internalCode != null ? internalCode : status.value())
                .errors(errors != null
                        ? errors.stream().filter(Objects::nonNull).map(Objects::toString).collect(Collectors.toList())
                        : null);

        return new ResponseEntity<>(response, headers, status);
    }
}
