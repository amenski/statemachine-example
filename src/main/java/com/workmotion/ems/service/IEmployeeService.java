package com.workmotion.ems.service;

import com.workmotion.ems.swagger.model.ModelEmployee;
import com.workmotion.ems.swagger.model.RequestSaveEmployee;
import com.workmotion.ems.util.EMSException;

public interface IEmployeeService {

    public abstract void addEmployee(RequestSaveEmployee data) throws EMSException;

    public abstract ModelEmployee getEmployee(Integer id) throws EMSException;

    public abstract void updateEmployeeStatus(Integer id, String status) throws EMSException;
}
