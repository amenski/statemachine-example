package com.workmotion.ems.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workmotion.ems.service.IEmployeeService;
import com.workmotion.ems.swagger.api.EmployeeApi;
import com.workmotion.ems.swagger.model.ModelEmployee;
import com.workmotion.ems.swagger.model.RequestSaveEmployee;
import com.workmotion.ems.swagger.model.ResponseBase;
import com.workmotion.ems.swagger.model.ResponseEmployee;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.util.ResponseHandler;

import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmployeeController  extends ResponseHandler implements EmployeeApi {

    private final IEmployeeService employeeService;
    
    @Override
    public ResponseEntity<ResponseBase> addEmployee(@ApiParam(value = ""  )  @Valid @RequestBody RequestSaveEmployee data) {
        HttpStatus status = HttpStatus.OK;
        Class<ResponseBase> responseClass = ResponseBase.class;
        ResponseBase response = new ResponseBase();
        
        try {
            employeeService.addEmployee(data);
            response = fillSuccessResponse(response);
        } catch (EMSException e) {
            log.error("Error creating employee. {}", e.toString());
            status = e.getHttpCode();
            response = fillFailResponseEMSException(responseClass, e);
        } catch (Exception e) {
            log.error("Error creating employee. {}", e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response = fillFailResponseGeneric(responseClass);
        }
        return new ResponseEntity<>(response, status);
    }

    @Override
    public ResponseEntity<ResponseBase> updateEmployeeStatus(
            @ApiParam(value = "",required=true) @PathVariable("id") Integer id,
            @ApiParam(value = "" ,required=true )  @Valid @RequestBody String status) {
        
        HttpStatus httpStatus = HttpStatus.OK;
        Class<ResponseBase> responseClass = ResponseBase.class;
        ResponseBase response = new ResponseBase();
        
        try {
            employeeService.updateEmployeeStatus(id, status);
            response = fillSuccessResponse(response);
        } catch (EMSException e) {
            log.error("Error updating employee status. {}", e.toString());
            httpStatus = e.getHttpCode();
            response = fillFailResponseEMSException(responseClass, e);
        } catch (Exception e) {
            log.error("Error updating employee status. {}", e.toString());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = fillFailResponseGeneric(responseClass);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
    
    @Override
    public ResponseEntity<ResponseEmployee> getEmployee(
            @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "employeeId", required = true) Integer employeeId) {
        HttpStatus httpStatus = HttpStatus.OK;
        Class<ResponseEmployee> responseClass = ResponseEmployee.class;
        ResponseEmployee response = new ResponseEmployee();
        
        try {
            ModelEmployee model = employeeService.getEmployee(employeeId);
            response = fillSuccessResponse(response.returnValue(model));
        } catch (EMSException e) {
            httpStatus = e.getHttpCode();
            response = fillFailResponseEMSException(responseClass, e);
        } catch (Exception e) {
            log.error("Error getting employee. {}", e.toString());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = fillFailResponseGeneric(responseClass);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
