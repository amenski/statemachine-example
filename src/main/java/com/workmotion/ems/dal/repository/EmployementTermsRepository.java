package com.workmotion.ems.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workmotion.ems.dal.model.EmployementTerms;

@Repository
public interface EmployementTermsRepository extends JpaRepository<EmployementTerms, Integer> {
}
