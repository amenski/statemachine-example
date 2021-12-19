package com.workmotion.ems.controller;

import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.ems.AssertTestHelper;
import com.workmotion.ems.service.EmployeeServiceImpl;
import com.workmotion.ems.swagger.model.RequestSaveEmployee;
import com.workmotion.ems.swagger.model.ResponseBase;
import com.workmotion.ems.util.ExceptionEnums;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().seed(42).build();

    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeService;

    @Spy
    private ModelMapper mapper;

    private ObjectMapper objMapper;

    @InjectMocks
    private EmployeeController controller;

    private static final String ADD_EMPLOYEE_URL = "/v1/employee";
    private static final String GET_EMPLOYEE_URL = "/v1/employee?employeeId=%s";
    private static final String UPDATE_CUSTOMER_URL = "/v1/employee/%s";

    private JacksonTester<RequestSaveEmployee> requestSaveEmployee;

    @BeforeEach
    public void init() {
        objMapper = new ObjectMapper();
        objMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void addEmployee_ThrowsEMSException() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw ExceptionEnums.EMPLOYEE_NOT_FOUND.get();
            }
        }).when(employeeService).addEmployee(ArgumentMatchers.any(RequestSaveEmployee.class));

        MockHttpServletResponse response = mockMvc.perform(post(ADD_EMPLOYEE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForEMSException(responseContent, ExceptionEnums.EMPLOYEE_NOT_FOUND);
    }

    @Test
    void addEmployee_ThrowsGenericException() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception();
            }
        }).when(employeeService).addEmployee(ArgumentMatchers.any(RequestSaveEmployee.class));

        MockHttpServletResponse response = mockMvc.perform(post(ADD_EMPLOYEE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForGenericException(responseContent);
    }

    @Test
    void addEmployee_Success() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        MockHttpServletResponse response = mockMvc.perform(post(ADD_EMPLOYEE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForSuccess(responseContent);
    }
    
    @Test
    void updateEmployeeStatus_ThrowsEMSException() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw ExceptionEnums.EMPLOYEE_NOT_FOUND.get();
            }
        }).when(employeeService).updateEmployeeStatus(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());

        MockHttpServletResponse response = mockMvc.perform(put(String.format(UPDATE_CUSTOMER_URL, 1))
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForEMSException(responseContent, ExceptionEnums.EMPLOYEE_NOT_FOUND);
    }

    @Test
    void updateEmployeeStatus_ThrowsGenericException() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception();
            }
        }).when(employeeService).updateEmployeeStatus(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());

        MockHttpServletResponse response = mockMvc.perform(put(String.format(UPDATE_CUSTOMER_URL, 1))
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForGenericException(responseContent);
    }

    @Test
    void updateEmployeeStatus_Success() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        MockHttpServletResponse response = mockMvc.perform(put(String.format(UPDATE_CUSTOMER_URL, 1))
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForSuccess(responseContent);
    }
    
    @Test
    void getEmployee_ThrowsEMSException() throws Exception {
        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw ExceptionEnums.EMPLOYEE_NOT_FOUND.get();
            }
        }).when(employeeService).getEmployee(ArgumentMatchers.anyInt());

        MockHttpServletResponse response = mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, 1))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForEMSException(responseContent, ExceptionEnums.EMPLOYEE_NOT_FOUND);
    }

    @Test
    void getEmployee_ThrowsGenericException() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception("Test.");
            }
        }).when(employeeService).getEmployee(ArgumentMatchers.anyInt());

        MockHttpServletResponse response = mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, 1))
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForGenericException(responseContent);
    }

    @Test
    void getEmployee_Success() throws Exception {
        // GIVEN
        RequestSaveEmployee request = random.nextObject(RequestSaveEmployee.class);

        // MOCK
        MockHttpServletResponse response = mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, 1))
                .contentType(MediaType.APPLICATION_JSON).content(requestSaveEmployee.write(request).getJson()))
                .andReturn().getResponse();

        ResponseBase responseContent = objMapper.readValue(response.getContentAsString(), ResponseBase.class);

        // VALIDATE
        AssertTestHelper.checkForSuccess(responseContent);
    }
}
