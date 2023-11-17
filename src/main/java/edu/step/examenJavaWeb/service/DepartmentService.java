package edu.step.examenJavaWeb.service;

import edu.step.examenJavaWeb.dao.DepartmentRepository;
import edu.step.examenJavaWeb.model.Department;
import edu.step.examenJavaWeb.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Department findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Department> findPage(int i) {
        Pageable page= PageRequest.of(i - 1, 10);
        return repository.findAll(page);
    }

    public Page<Department> searchByKeywordIgnoreCaseAndPartialMatch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        String formattedKeyword = "%" + keyword.toLowerCase() + "%";
        return repository.findDepartmentsByKeywordIgnoreCaseAndPartialMatch(formattedKeyword, pageable);
    }

    public void save(Department department) {
        repository.save(department);
    }

    public void update(Integer id, Department updatedDepartment) {
        Department existingDepartment = repository.findById(id).orElse(null);
        if (existingDepartment != null && updatedDepartment != null) {
            existingDepartment.setName(updatedDepartment.getName());
            existingDepartment.setCompany(updatedDepartment.getCompany());
            repository.save(existingDepartment);
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Department> getAllDepartments() {
        return repository.findAll();
    }
}

