package com.workmotion.ems.workflow;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory
public class WorkflowStateMachineConfiguration extends StateMachineConfigurerAdapter<WorkFlowStates, WorkFlowEvents> {

    private final JpaPersistingStateMachineInterceptor<WorkFlowStates, WorkFlowEvents, String> persister;
    @Override
    public void configure(StateMachineConfigurationConfigurer<WorkFlowStates, WorkFlowEvents> config)
            throws Exception {
        config
            .withPersistence()
                .runtimePersister(persister);
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
                    .source(WorkFlowStates.ADDED).target(WorkFlowStates.IN_CHECK).event(WorkFlowEvents.ADDED_TO_IN_CHECK)
                 .and()
                .withExternal()
                    .source(WorkFlowStates.SECURITY_CHECK_STARTED).target(WorkFlowStates.SECURITY_CHECK_FINISHED).event(WorkFlowEvents.SECURITY_CHECK_STARTED_TO_SECURITY_CHECK_FINISHED)
                .and()
                .withExternal()
                    .source(WorkFlowStates.WORK_PERMIT_CHECK_STARTED).target(WorkFlowStates.WORK_PERMIT_CHECK_FINISHED).event(WorkFlowEvents.WORK_PERMIT_CHECK_STARTED_TO_WORK_PERMIT_CHECK_FINISHED)
                .and()
                .withExternal()
                    .source(WorkFlowStates.APPROVED).target(WorkFlowStates.IN_CHECK).event(WorkFlowEvents.APPROVED_TO_IN_CHECK)
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
