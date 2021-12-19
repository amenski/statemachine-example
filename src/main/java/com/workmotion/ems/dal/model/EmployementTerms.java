package com.workmotion.ems.dal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee_terms")
@NamedQuery(name = "EmployementTerms.findAll", query = "SELECT c FROM EmployementTerms c")
public class EmployementTerms implements Serializable {
    private static final long serialVersionUID = 9137828160744191460L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "skill_requirement")
    private String skillRequirement;

    @Column(name = "education_requirement")
    private String educationRequirement;

    @Column(name = "annual_salary")
    private Double annualSalary;
}
