package com.workmotion.ems.service;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.util.EMSException;

public interface IWorkFlowService {
    public boolean executeTransition(Employee info, String event) throws EMSException;
}
