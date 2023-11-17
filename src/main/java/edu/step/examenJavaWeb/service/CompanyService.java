package edu.step.examenJavaWeb.service;

import edu.step.examenJavaWeb.dao.CompanyRepository;
import edu.step.examenJavaWeb.model.Company;
import edu.step.examenJavaWeb.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyService {

    @Autowired
    private CompanyRepository repository;

    public List<Company> findAll() {
        return repository.findAll();
    }

    public Company findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Company> findPage(int i) {
        Pageable page= PageRequest.of(i - 1, 10);
        return repository.findAll(page);
    }

    public void save(Company company) {
        repository.save(company);
    }

    public void update(Integer id, Company updatedCompany) {
        Company existingCompany = repository.findById(id).orElse(null);
        if (existingCompany != null && updatedCompany != null) {
            existingCompany.setName(updatedCompany.getName());
            // Добавьте другие поля для обновления, если необходимо
            repository.save(existingCompany);
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Company> searchByKeywordIgnoreCaseAndPartialMatch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        String formattedKeyword = "%" + keyword.toLowerCase() + "%";
        return repository.findDepartmentsByKeywordIgnoreCaseAndPartialMatch(formattedKeyword, pageable);
    }

    public List<Company> getAllDepartments() {
        return repository.findAll();
    }
}

