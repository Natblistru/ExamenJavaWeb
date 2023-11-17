package edu.step.examenJavaWeb.dao;

import edu.step.examenJavaWeb.model.Company;
import edu.step.examenJavaWeb.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findEmployeesByNameLike(String name);

    @Query("SELECT e FROM Company e " +
            "WHERE LOWER(e.name) LIKE :keyword ")
    Page<Company> findDepartmentsByKeywordIgnoreCaseAndPartialMatch(
            @Param("keyword") String keyword, Pageable pageable);
}
