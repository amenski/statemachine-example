package com.workmotion.ems.service;

import com.workmotion.ems.util.EMSException;

public class WorkFlowServiceImpl implements IWorkFlowService {

    @Override
    public boolean executeTransition(Object info, Integer toState)  throws EMSException {
        return false;
    }

}
