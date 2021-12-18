package com.workmotion.ems.workflow;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkFlowStateMachineListener extends StateMachineListenerAdapter<WorkFlowStates, WorkFlowEvents>  {
    
    @Override
    public void stateChanged(State<WorkFlowStates, WorkFlowEvents> from, State<WorkFlowStates, WorkFlowEvents> to) {
        super.stateChanged(from, to);
        log.info("--------------------- State change successfull --------------------- {}", to.getId().name());
    }

    @Override
    public void eventNotAccepted(Message<WorkFlowEvents> event) {
        super.eventNotAccepted(event);
        log.info("====================== State change failed ==================");
    }
}