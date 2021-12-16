package com.workmotion.ems.service;

import com.workmotion.ems.util.EMSException;

public interface IWorkFlowService {
    public boolean executeTransition(Object info, Integer toState) throws EMSException;
}
