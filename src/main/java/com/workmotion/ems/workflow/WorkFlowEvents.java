package com.workmotion.ems.workflow;

public enum WorkFlowEvents {
    IN_CHECK,
    SECURITY_CHECK_FINISH,
    WORK_PERMIT_CHECK_FINISH,
    APPROVE,
    ACTIVATE;
    
    public static WorkFlowEvents get(final String event) {
        for(WorkFlowEvents ev : WorkFlowEvents.values()) {
            if(ev.name().equals(event))
                return ev;
        }
        return null;
    }
}
