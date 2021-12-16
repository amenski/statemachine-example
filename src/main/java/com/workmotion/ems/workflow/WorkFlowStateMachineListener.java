package com.workmotion.ems.workflow;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkFlowStateMachineListener extends StateMachineListenerAdapter<WorkFlowStates, WorkFlowEvents>  {
    
    @Override
    public void stateChanged(State<WorkFlowStates, WorkFlowEvents> from, State<WorkFlowStates, WorkFlowEvents> to) {
        log.info("State change successfull");
        super.stateChanged(from, to);
    }

    @Override
    public void eventNotAccepted(Message<WorkFlowEvents> event) {
        log.info("State change unsuccessfull");
        super.eventNotAccepted(event);
    }
}