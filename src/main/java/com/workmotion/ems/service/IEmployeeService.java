package com.workmotion.ems.service;

import com.workmotion.ems.util.EMSException;

import it.aman.ethjournal.swagger.model.RequestSaveEmployee;

public interface IEmployeeService {

    public abstract void addEmployee(RequestSaveEmployee data) throws EMSException;

    public abstract void getEmployee(String employeeId) throws EMSException;

    public abstract void updateEmployeeStatus(Integer id, String status) throws EMSException;
}
