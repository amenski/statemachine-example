package com.workmotion.ems.dal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.workmotion.ems.util.EmployeeStatusAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee")
@NamedQuery(name = "Employee.findAll", query = "SELECT c FROM Employee c")
public class Employee implements Serializable {
    private static final long serialVersionUID = 849343719056973592L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private LocalDate dob;
    
    private String gender;
    
    private String occupation;
    
    @Column(name = "passport_number")
    private String passportNumber;
    
    @ManyToOne
    @JoinColumn(name = "employement_terms_id")
    private EmployementTerms employementTerms;
    
    @Convert(converter = EmployeeStatusAttributeConverter.class)
    private List<String> status;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;
}
