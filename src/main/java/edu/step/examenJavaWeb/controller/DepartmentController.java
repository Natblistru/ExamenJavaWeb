package edu.step.examenJavaWeb.controller;

import edu.step.examenJavaWeb.model.Company;
import edu.step.examenJavaWeb.model.Department;
import edu.step.examenJavaWeb.model.User;
import edu.step.examenJavaWeb.service.CompanyService;
import edu.step.examenJavaWeb.service.DepartmentService;
import edu.step.examenJavaWeb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/departments")
    public String index(
            @RequestParam(value = "p", defaultValue = "1") Integer pageParam,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        Page<Department> page;

        if (search != null && !search.isEmpty()) {
            page = departmentService.searchByKeywordIgnoreCaseAndPartialMatch(search, pageParam);
        } else {
            page = departmentService.findPage(pageParam);
        }

        //Inscriptia "Bun venit, Nume Prenume"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("list", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("activePage", pageParam);
        return "departments";
    }

    @GetMapping("/add/department")
    public String createDepartment(Model model) {
        Department department = new Department();
        List<Company> companies = companyService.getAllDepartments();
        model.addAttribute("companies", companies);

        model.addAttribute("department", department);
        return "adddepartment";
    }

    @PostMapping("/add/department")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/department/{id}")
    public String editDepartment(@PathVariable Integer id, Model model) {
        Department department = departmentService.findById(id);
        if (department == null) {
            return "redirect:/departments";
        }
        List<Company> companies = companyService.getAllDepartments();
        model.addAttribute("companies", companies);
        model.addAttribute("department", department);
        return "editdepartment";
    }

    @PostMapping("/edit/department/{id}")
    public String updateDepartment(@PathVariable Integer id, @ModelAttribute("department") Department department) {
        departmentService.update(id, department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/department/{id}")
    public String confirmDeleteDepartment(@PathVariable Integer id, Model model) {
        Department department = departmentService.findById(id);
        model.addAttribute("department", department);
        return "deletedepartment";
    }

    @PostMapping("/delete/department/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        departmentService.delete(id);
        return "redirect:/departments";
    }
}

