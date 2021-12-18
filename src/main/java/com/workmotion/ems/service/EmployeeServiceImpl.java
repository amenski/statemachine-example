package com.workmotion.ems.service;

import java.time.OffsetDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.dal.model.EmployementTerms;
import com.workmotion.ems.dal.repository.EmployeeRepository;
import com.workmotion.ems.dal.repository.EmployementTermsRepository;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.util.ExceptionEnums;
import com.workmotion.ems.workflow.WorkFlowStates;

import it.aman.ethjournal.swagger.model.RequestSaveEmployee;
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
    public void addEmployee(RequestSaveEmployee data) throws EMSException {
        log.debug("Creating new employee: {}", data);
        try {
            if(data == null) {
                throw ExceptionEnums.INVALID_INPUT_EXCEPTION.get();
            }
            
            OffsetDateTime now = OffsetDateTime.now();
            Employee employee = mapper.map(data, Employee.class);
            employee.setCreatedAt(now);
            employee.setModifiedAt(now);
            employee.setStatus(WorkFlowStates.ADDED.name());
            
            if(data.getEmployementTermsId() == null) {
                throw ExceptionEnums.INVALID_INPUT_EXCEPTION.get().errorMessage("Contract id can not be empty.");
            }
            
            EmployementTerms contract = employementTermsRepository.findById(data.getEmployementTermsId()).orElse(null);
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
    public void updateEmployeeStatus(Integer id, String status) throws EMSException {
        final String methodName = "updateEmployeeStatus()";
        log.info("Updating empoyeeId:{} status to: {}", id, status);
        try {
            Employee employee = employeeRepository.findById(id).orElseThrow(ExceptionEnums.EMPLOYEE_NOT_FOUND);
            boolean accepted = workFlowService.executeTransition(employee, WorkFlowStates.get(status));
            if(accepted) {
                // do return
            }
        } catch (EMSException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} Error updating employee status: {}", methodName, e.toString());
            throw e;
        }
    }
    
    @Override
    public void getEmployee(String employeeId) throws EMSException {
    }


}
