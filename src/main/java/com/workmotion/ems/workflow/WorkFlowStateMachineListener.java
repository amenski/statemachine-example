package com.workmotion.ems.workflow;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class WorkFlowStateMachineListener implements StateMachineListener<WorkFlowStates, WorkFlowEvents> {

    @Override
    public void stateChanged(State<WorkFlowStates, WorkFlowEvents> from, State<WorkFlowStates, WorkFlowEvents> to) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateEntered(State<WorkFlowStates, WorkFlowEvents> state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateExited(State<WorkFlowStates, WorkFlowEvents> state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void eventNotAccepted(Message<WorkFlowEvents> event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transition(Transition<WorkFlowStates, WorkFlowEvents> transition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transitionStarted(Transition<WorkFlowStates, WorkFlowEvents> transition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transitionEnded(Transition<WorkFlowStates, WorkFlowEvents> transition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateMachineStarted(StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateMachineStopped(StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateMachineError(StateMachine<WorkFlowStates, WorkFlowEvents> stateMachine, Exception exception) {
        // TODO Auto-generated method stub

    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateContext(StateContext<WorkFlowStates, WorkFlowEvents> stateContext) {
        // TODO Auto-generated method stub

    }

}
