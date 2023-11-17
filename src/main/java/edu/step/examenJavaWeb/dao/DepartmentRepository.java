package edu.step.examenJavaWeb.dao;

import edu.step.examenJavaWeb.model.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findEmployeesByNameLike(String name);

    @Query("SELECT e FROM Department e " +
            "WHERE LOWER(e.name) LIKE :keyword " +
            "OR LOWER(e.company.name) LIKE :keyword")
    Page<Department> findDepartmentsByKeywordIgnoreCaseAndPartialMatch(
            @Param("keyword") String keyword, Pageable pageable);
}
