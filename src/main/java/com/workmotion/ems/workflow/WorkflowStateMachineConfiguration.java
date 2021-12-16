package com.workmotion.ems.workflow;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;


@Configuration
@EnableStateMachine
public class WorkflowStateMachineConfiguration extends StateMachineConfigurerAdapter<WorkFlowStates, WorkFlowEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<WorkFlowStates, WorkFlowEvents> config)
            throws Exception {
        config
            .withConfiguration()
            .autoStartup(true)
            .listener(new WorkFlowStateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<WorkFlowStates, WorkFlowEvents> states) throws Exception {
        states
                .withStates()
                .initial(WorkFlowStates.ADDED)
                .fork(WorkFlowStates.IN_CHECK)
                .join(WorkFlowStates.APPROVED)
                .end(WorkFlowStates.ACTIVE)
                .and()
                .withStates()
                    .parent(WorkFlowStates.IN_CHECK)
                    .initial(WorkFlowStates.SECURITY_CHECK_STARTED)
                    .end(WorkFlowStates.SECURITY_CHECK_FINISHED)
                .and()
                .withStates()
                    .parent(WorkFlowStates.IN_CHECK)
                    .initial(WorkFlowStates.WORK_PERMIT_CHECK_STARTED)
                    .end(WorkFlowStates.WORK_PERMIT_CHECK_FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkFlowStates, WorkFlowEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(WorkFlowStates.ADDED).target(WorkFlowStates.IN_CHECK).event(WorkFlowEvents.IN_CHECK)
                .and()
                .withExternal()
                    .source(WorkFlowStates.SECURITY_CHECK_STARTED).target(WorkFlowStates.SECURITY_CHECK_FINISHED).event(WorkFlowEvents.SECURITY_CHECK_FINISH)
                .and()
                .withExternal()
                    .source(WorkFlowStates.WORK_PERMIT_CHECK_STARTED).target(WorkFlowStates.WORK_PERMIT_CHECK_FINISHED).event(WorkFlowEvents.WORK_PERMIT_CHECK_FINISH)
                .and()
                .withFork()
                    .source(WorkFlowStates.IN_CHECK)
                    .target(WorkFlowStates.SECURITY_CHECK_STARTED)
                    .target(WorkFlowStates.WORK_PERMIT_CHECK_STARTED)
                .and()
                .withJoin()
                    .source((WorkFlowStates.SECURITY_CHECK_FINISHED))
                    .source(WorkFlowStates.WORK_PERMIT_CHECK_FINISHED)
                    .target(WorkFlowStates.APPROVED);
    }
}
