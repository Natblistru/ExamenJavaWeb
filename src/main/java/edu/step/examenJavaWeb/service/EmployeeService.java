package edu.step.examenJavaWeb.service;

import edu.step.examenJavaWeb.dao.EmployeeRepository;
import edu.step.examenJavaWeb.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public List<Employee> findAll() {

        return repository.findAll();
    }

    public Employee findById(Integer id) {
        return repository.findById(id)
                .orElse(null);
    }

    public List<Employee> findByName(String name) {
        return repository.findEmployeesByNameLike(name);
    }

    public Page<Employee> findPage(int i) {
        Pageable page= PageRequest.of(i - 1, 6);
        return repository.findAll(page);
    }

    public Page<Employee> findPage(int page, Integer sortColumnIndex, String sortOrder) {
        return performSearch(null, page, sortColumnIndex, sortOrder);
    }

    public Page<Employee> searchByKeywordIgnoreCaseAndPartialMatch(String keyword, int page, Integer sortColumnIndex, String sortOrder) {
        return performSearch(keyword, page, sortColumnIndex, sortOrder);
    }

    private Page<Employee> performSearch(String keyword, int page, Integer sortColumnIndex, String sortOrder) {
        Sort sort;
        String[] sortProperties = {"name", "surname", "birthdate", "department.name", "department.company.name"};

        if (sortColumnIndex != null) {
            String sortProperty = sortProperties[sortColumnIndex];
            sort = Sort.by(sortProperty);

            // Inversăm ordinea sortării dacă sortOrder este DESC
            if (EmployeeService.DESC.equals(sortOrder)) {
                sort = sort.descending();
            }
        } else {
            sort = Sort.unsorted();
        }

        Pageable pageable = PageRequest.of(page - 1, 6, sort);
        Page<Employee> result;

        if (keyword != null && !keyword.isEmpty()) {
            String formattedKeyword = "%" + keyword.toLowerCase() + "%";
            result = repository.findEmployeesByKeywordIgnoreCaseAndPartialMatch(formattedKeyword, pageable);
        } else {
            result = repository.findAll(pageable);
        }

        return result;
    }

    public void save(Employee employee) {
        repository.save(employee);
    }

    public void update(Integer id, Employee updatedEmployee) {
        Employee existingEmployee = repository.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setSurname(updatedEmployee.getSurname());
            existingEmployee.setBirthdate(updatedEmployee.getBirthdate());
            existingEmployee.setDepartment(updatedEmployee.getDepartment());
            repository.save(existingEmployee);
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}

