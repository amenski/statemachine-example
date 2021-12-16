package com.workmotion.ems.service;

public interface IWorkFlowService {
    public boolean executeTransition(Object info, Integer toState);
}
