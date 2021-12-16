package com.workmotion.ems.workflow;

public enum WorkFlowEvents {
    ADD, 
    IN_CHECK, 
    APPROVE_TO_IN_CHECK,
    APPROVE, 
    ACTIVATE, 
    SECURITY_CHECK_START,
    SECURITY_CHECK_FINISH, 
    WORK_PERMIT_CHECK_START,
    WORK_PERMIT_CHECK_FINISH;
    
    public static WorkFlowEvents get(final String event) {
        for(WorkFlowEvents ev : WorkFlowEvents.values()) {
            if(ev.name().equals(event))
                return ev;
        }
        return ADD;
    }
}
