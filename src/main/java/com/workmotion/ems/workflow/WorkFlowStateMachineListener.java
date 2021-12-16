package com.workmotion.ems.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class WorkFlowStateMachineListener extends StateMachineListenerAdapter<WorkFlowStates, WorkFlowEvents>  {
    private static final Logger logger = LoggerFactory.getLogger(WorkFlowStateMachineListener.class);
    
    @Override
    public void stateChanged(State<WorkFlowStates, WorkFlowEvents> from, State<WorkFlowStates, WorkFlowEvents> to) {
        logger.info("State change successfull");
        super.stateChanged(from, to);
    }

    @Override
    public void eventNotAccepted(Message<WorkFlowEvents> event) {
        logger.info("State change unsuccessfull");
        super.eventNotAccepted(event);
    }
}