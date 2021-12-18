package com.workmotion.ems.service;

import com.workmotion.ems.dal.model.Employee;
import com.workmotion.ems.util.EMSException;
import com.workmotion.ems.workflow.WorkFlowStates;

public interface IWorkFlowService {
    public boolean executeTransition(Employee info, WorkFlowStates state) throws EMSException;
}
