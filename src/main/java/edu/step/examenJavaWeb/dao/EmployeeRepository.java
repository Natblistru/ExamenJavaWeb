package edu.step.examenJavaWeb.dao;

import edu.step.examenJavaWeb.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findEmployeesByNameLike(String name);

    @Query("SELECT e FROM Employee e " +
            "WHERE LOWER(e.name) LIKE :keyword " +
            "OR LOWER(e.surname) LIKE :keyword " +
            "OR LOWER(e.department.name) LIKE :keyword " +
            "OR LOWER(e.department.company.name) LIKE :keyword")
    Page<Employee> findEmployeesByKeywordIgnoreCaseAndPartialMatch(
            @Param("keyword") String keyword, Pageable pageable);





}

