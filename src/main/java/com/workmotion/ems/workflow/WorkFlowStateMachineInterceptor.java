package com.workmotion.ems.workflow;

import java.util.Iterator;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.dal.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Intercept and manage update operations
 * 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkFlowStateMachineInterceptor extends StateMachineInterceptorAdapter<WorkFlowStates, WorkFlowEvents> {

    private final EmployeeRepository employeeRepository;

    @Override
    public void postStateChange(State<WorkFlowStates, WorkFlowEvents> state, Message<WorkFlowEvents> message,
            Transition<WorkFlowStates, WorkFlowEvents> transition,
            StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine,
            StateMachine<WorkFlowStates, WorkFlowEvents> rootStateMachine) {
        
        Employee employee = employeeRepository.findById(Integer.valueOf(rootStateMachine.getId())).orElse(null);

        if (employee == null) {
            log.error("Employee not found, unable to update status.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        Iterator<WorkFlowStates> statesIterator = rootStateMachine.getState().getIds().iterator();
        while (statesIterator.hasNext()) {
            builder.append(statesIterator.next().name());
            if (statesIterator.hasNext()) {
                builder.append("#");
            }
        }
        employee.setStatus(builder.toString());
        employeeRepository.save(employee);
    }

    @Override
    public Exception stateMachineError(StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine, Exception exception) {
        log.error("{} StateMachine encountered error: [ message: {}]", "stateMachineError()", exception.getMessage());
        return super.stateMachineError(stateMachine, exception);
    }

}
