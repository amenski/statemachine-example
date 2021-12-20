package com.workmotion.ems;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.dal.model.EmployementTerms;
import com.workmotion.ems.dal.repository.EmployeeRepository;
import com.workmotion.ems.service.WorkFlowServiceImpl;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.util.ExceptionEnums;
import com.workmotion.ems.workflow.WorkFlowEvents;
import com.workmotion.ems.workflow.WorkFlowStateMachineInterceptor;
import com.workmotion.ems.workflow.WorkFlowStates;

import io.github.benas.randombeans.api.EnhancedRandom;

@SpringBootTest
@AutoConfigureMockMvc
class EmsApplicationTests {

    @Autowired
    StateMachineService<WorkFlowStates, WorkFlowEvents> stateMachineService;

    @Autowired
    WorkFlowStateMachineInterceptor stateMachineInterceptor;

    @Autowired
    WorkFlowServiceImpl workFlowService;

    @Autowired
    EmployeeRepository employeeRepository;

    private StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine;

    @BeforeEach
    public void setup() throws Exception {
        stateMachine = stateMachineService.acquireStateMachine(EnhancedRandom.random(String.class));
    }

    @Test
    void executeTransition_ThrowWorkFlowEventNotFound() throws Exception {
        // given
        Employee empl = buildMockEmployee();

        // validate
        assertThrows(EMSException.class, () -> workFlowService.executeTransition(empl, "EVENT_NOT_FOUND"),
                ExceptionEnums.STATE_TRANSITION_EXCEPTION.get().getErrorMessage());
    }

    @Test
    void executeTransition_TransitionFails() throws Exception {
        // given
        Employee empl = buildMockEmployee();

        boolean success = workFlowService.executeTransition(empl, WorkFlowEvents.APPROVE.name());

        // validate
        assertFalse(success);
    }

    @Test
    void executeTransition_ADDED_Success() throws Exception {
        StateMachineTestPlan<WorkFlowStates, WorkFlowEvents> plan = StateMachineTestPlanBuilder
                .<WorkFlowStates, WorkFlowEvents>builder().stateMachine(stateMachine).step()
                .expectState(WorkFlowStates.ADDED).and().build();
        plan.test();
    }

    @Test
    void executeTransition_INCECK_Success() throws Exception {
        StateMachineTestPlan<WorkFlowStates, WorkFlowEvents> plan = StateMachineTestPlanBuilder
                .<WorkFlowStates, WorkFlowEvents>builder().stateMachine(stateMachine).step()
                .sendEvent(MessageBuilder.withPayload(WorkFlowEvents.IN_CHECK).build())
                .expectStates(WorkFlowStates.IN_CHECK, WorkFlowStates.SECURITY_CHECK_STARTED,
                        WorkFlowStates.WORK_PERMIT_CHECK_STARTED)
                .expectStateChanged(2).and().build();
        plan.test();
    }

    @Test
    void executeTransition_APPROVE_Success() throws Exception {
        StateMachineTestPlan<WorkFlowStates, WorkFlowEvents> plan = StateMachineTestPlanBuilder
                .<WorkFlowStates, WorkFlowEvents>builder().stateMachine(stateMachine).step()
                .sendEvent(MessageBuilder.withPayload(WorkFlowEvents.IN_CHECK).build())
                .expectStates(WorkFlowStates.IN_CHECK, WorkFlowStates.SECURITY_CHECK_STARTED,
                        WorkFlowStates.WORK_PERMIT_CHECK_STARTED)
                .expectStateChanged(2).and().step()
                .sendEvent(MessageBuilder.withPayload(WorkFlowEvents.SECURITY_CHECK_FINISH).build())
                .expectStates(WorkFlowStates.IN_CHECK, WorkFlowStates.SECURITY_CHECK_FINISHED,
                        WorkFlowStates.WORK_PERMIT_CHECK_STARTED)
                .expectStateChanged(1).and().step()
                .sendEvent(MessageBuilder.withPayload(WorkFlowEvents.WORK_PERMIT_CHECK_FINISH).build())
                .expectStates(WorkFlowStates.APPROVED).expectStateChanged(2).and().build();
        plan.test();
    }

    // =======
    private Employee buildMockEmployee() {
        List<String> status = new ArrayList<String>();
        status.add(WorkFlowStates.ADDED.name());

        OffsetDateTime now = OffsetDateTime.now();

        Employee empl = new Employee();
        empl.setId(1);
        empl.setFirstName("first_name");
        empl.setLastName("last_name");
        empl.setPassportNumber("passport_no");
        empl.setStatus(status);
        empl.setDob(LocalDate.of(1900, 5, 1));
        empl.setCreatedAt(now);
        empl.setModifiedAt(now);
        empl.setEmployementTerms(buildMockEmployementTerms());

        return empl;
    }

    private EmployementTerms buildMockEmployementTerms() {
        EmployementTerms terms = new EmployementTerms();
        terms.setId(2);
        terms.setJobTitle("Job title");
        terms.setJobDescription("Description");
        terms.setSkillRequirement("Skills");
        terms.setEducationRequirement("Education requirement");

        return terms;
    }

}
