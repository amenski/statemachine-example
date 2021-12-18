package com.workmotion.ems.workflow;

public enum WorkFlowEvents {
    ADDED_TO_IN_CHECK, 
    SECURITY_CHECK_STARTED_TO_SECURITY_CHECK_FINISHED,
    WORK_PERMIT_CHECK_STARTED_TO_WORK_PERMIT_CHECK_FINISHED,
    APPROVED_TO_IN_CHECK,
    APPROVED_TO_ACTIVE;
    
    public static WorkFlowEvents get(final String src, final String dst) {
        final String event = src + "_TO_" + dst;
        for(WorkFlowEvents ev : WorkFlowEvents.values()) {
            if(ev.name().equals(event))
                return ev;
        }
        return null;
    }
}
