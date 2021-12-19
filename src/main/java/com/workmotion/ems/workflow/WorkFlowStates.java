package com.workmotion.ems.workflow;

public enum WorkFlowStates {
    ADDED, 
    IN_CHECK, 
    APPROVED,
    JOIN,
    ACTIVE, 
    SECURITY_CHECK_STARTED,
    SECURITY_CHECK_FINISHED, 
    WORK_PERMIT_CHECK_STARTED,
    WORK_PERMIT_CHECK_FINISHED;
    
    public static WorkFlowStates get(final String state) {
        for(WorkFlowStates st : WorkFlowStates.values()) {
            if(st.name().equals(state))
                return st;
        }
        return null;
    }
}
