package com.workmotion.ems.service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.dal.model.EmployementTerms;
import com.workmotion.ems.dal.repository.EmployeeRepository;
import com.workmotion.ems.dal.repository.EmployementTermsRepository;
import com.workmotion.ems.swagger.model.ModelEmployee;
import com.workmotion.ems.swagger.model.ModelEmployementTerms;
import com.workmotion.ems.swagger.model.RequestSaveEmployee;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.util.ExceptionEnums;
import com.workmotion.ems.workflow.WorkFlowStates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final EmployementTermsRepository employementTermsRepository;
    
    private final IWorkFlowService workFlowService;
    
    @Override
    public void addEmployee(final RequestSaveEmployee data) throws EMSException {
        log.debug("Creating new employee: {}", data);
        try {
            if(data == null) {
                throw ExceptionEnums.INVALID_INPUT_EXCEPTION.get();
            }
            
            OffsetDateTime now = OffsetDateTime.now();
            Employee employee = mapper.map(data, Employee.class);
            employee.setCreatedAt(now);
            employee.setModifiedAt(now);
            employee.setStatus(Arrays.asList(WorkFlowStates.ADDED.name()));
            
            Integer id = Optional.ofNullable(data.getEmployementTerms()).map(ModelEmployementTerms::getId).orElse(null);
            if(id == null) {
                throw ExceptionEnums.INVALID_INPUT_EXCEPTION.get().errorMessage("Contract id can not be empty.");
            }
            
            EmployementTerms contract = employementTermsRepository.findById(id).orElse(null);
            if(contract == null) {
                throw ExceptionEnums.CONTRACT_NOT_FOUND.get();
            }
            employeeRepository.save(employee);
        } catch (EMSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving employee data: {}", e.toString());
            throw e;
        }
    }

    @Override
    public void updateEmployeeStatus(final Integer id, final String event) throws EMSException {
        final String methodName = "updateEmployeeStatus()";
        log.info("Updating empoyeeId:{} status to: {}", id, event);
        try {
            Employee employee = employeeRepository.findById(id).orElseThrow(ExceptionEnums.EMPLOYEE_NOT_FOUND);
            boolean accepted = workFlowService.executeTransition(employee, event);
            if(!accepted) {
                log.error("{} Status update failed with event: {}.", methodName, event);
                throw ExceptionEnums.STATE_TRANSITION_EXCEPTION.get();
            }
        } catch (EMSException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} Error updating employee status: {}", methodName, e.toString());
            throw e;
        }
    }
    
    @Override
    public ModelEmployee getEmployee(final Integer id) throws EMSException {
        final String methodName = "getEmployee()";
        log.info("{} Get employee empoyeeId: {}", methodName, id);
        try {
            Employee employee = employeeRepository.findById(id).orElseThrow(ExceptionEnums.EMPLOYEE_NOT_FOUND);
            ModelEmployementTerms terms = mapper.map(employee.getEmployementTerms(), ModelEmployementTerms.class);
            ModelEmployee modelEmployee = mapper.map(employee, ModelEmployee.class);
            modelEmployee.setEmployementTerms(terms);
            return modelEmployee;
        } catch (EMSException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} Error updating employee status: {}", methodName, e.toString());
            throw e;
        }
    }


}
