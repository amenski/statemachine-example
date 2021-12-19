package com.workmotion.ems.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.dal.model.EmployementTerms;
import com.workmotion.ems.dal.repository.EmployeeRepository;
import com.workmotion.ems.dal.repository.EmployementTermsRepository;
import com.workmotion.ems.swagger.model.ModelEmployee;
import com.workmotion.ems.swagger.model.RequestSaveEmployee;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.util.ExceptionEnums;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    
    private static final String EVENT = "EVENT";

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().seed(42).build();

    @Spy
    private ModelMapper mapper;

    @Mock
    EmployementTermsRepository employementTermsRepository;
    
    @Mock
    EmployeeRepository employeeRepository;
    
    @Mock
    WorkFlowServiceImpl workFlowService;
    
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    @Test
    void addEmployee_ThrowOnInvalidInput1() throws Exception {
        // TEST
        assertThrows(EMSException.class, () -> employeeService.addEmployee(null), ExceptionEnums.INVALID_INPUT_EXCEPTION.get().getErrorMessage());
    }
    
    @Test
    void addEmployee_ThrowOnInvalidInput2() throws Exception {
        //GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);
        request.getEmployementTerms().setId(null);
        
        // TEST
        assertThrows(EMSException.class, () -> employeeService.addEmployee(request), ExceptionEnums.INVALID_INPUT_EXCEPTION.get().getErrorMessage());
    }
    
    @Test
    void addEmployee_ThrowOnContractNotFound() throws Exception {
        //GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);
        
        // MOCK
        when(employementTermsRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
        
        // TEST
        assertThrows(EMSException.class, () -> employeeService.addEmployee(request), ExceptionEnums.CONTRACT_NOT_FOUND.get().getErrorMessage());
    }
    
    @Test
    void addEmployee_Success() throws Exception {
        //GIVEN
        Employee empl               = random.nextObject(Employee.class);
        EmployementTerms  terms     = random.nextObject(EmployementTerms.class);
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);
        
        // MOCK
        when(employementTermsRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(terms));
        when(employeeRepository.save(ArgumentMatchers.any())).thenReturn(empl);
        
        // TEST
        assertDoesNotThrow(() -> employeeService.addEmployee(request));
    }
    
    @Test
    void updateEmployeeStatus_ThrowEmployeeNotFound() throws Exception {
        // TEST
        assertThrows(EMSException.class, () -> employeeService.updateEmployeeStatus(1, EVENT), ExceptionEnums.EMPLOYEE_NOT_FOUND.get().getErrorMessage());
    }
    
    @Test
    void updateEmployeeStatus_TransitionFail() throws Exception {
        //GIVEN
        Employee empl = random.nextObject(Employee.class);
        
        // MOCK
        when(employeeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(empl));
        when(workFlowService.executeTransition(empl, EVENT)).thenReturn(false);
        
        // TEST
        assertThrows(EMSException.class, () -> employeeService.updateEmployeeStatus(1, EVENT), ExceptionEnums.STATE_TRANSITION_EXCEPTION.get().getErrorMessage());
    }
    
    @Test
    void updateEmployeeStatus_Success() throws Exception {
        //GIVEN
        Employee empl = random.nextObject(Employee.class);
        
        // MOCK
        when(employeeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(empl));
        when(workFlowService.executeTransition(empl, EVENT)).thenReturn(true);
        
        // TEST
        assertDoesNotThrow(() -> employeeService.updateEmployeeStatus(1, EVENT));
    }
    
    @Test
    void getEmployee_ThrowEmployeeNotFound() throws Exception {
        // TEST
        assertThrows(EMSException.class, () -> employeeService.getEmployee(1), ExceptionEnums.EMPLOYEE_NOT_FOUND.get().getErrorMessage());
    }

    @Test
    void getEmployee_ThrowGenericException() throws Exception {
        //GIVEN
        Employee empl = random.nextObject(Employee.class);
        empl.setEmployementTerms(null);
        
        // MOCK
        when(employeeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(empl));
        
        // TEST
        assertThrows(Exception.class, () -> employeeService.getEmployee(1));
    }
    
    @Test
    void getEmployee_Success() throws Exception {
        //GIVEN
        Employee empl = random.nextObject(Employee.class);
        
        // MOCK
        when(employeeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(empl));
        
        // TEST
        ModelEmployee response = employeeService.getEmployee(1);
        
        assertNotNull(response);
        assertEquals(empl.getId(), response.getId());
    }
}
