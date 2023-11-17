package edu.step.examenJavaWeb.controller;

import edu.step.examenJavaWeb.dao.UserRepository;
import edu.step.examenJavaWeb.model.Department;
import edu.step.examenJavaWeb.model.Employee;
import edu.step.examenJavaWeb.model.User;
import edu.step.examenJavaWeb.service.DepartmentService;
import edu.step.examenJavaWeb.service.EmployeeService;
import edu.step.examenJavaWeb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/employees")
    public String index(
            @RequestParam(value = "p", defaultValue = "1") Integer pageParam,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) Integer sortColumn,
            HttpSession session,
            Model model) {
        Page<Employee> page;

        // Dacă nu există sortColumn în sesiune, sortColumn diferit sau pageParam diferit față de cel primit ca parametru, setăm sortare ASC
        String sortOrder = EmployeeService.ASC;
        if (sortColumn == null || !sortColumn.equals(session.getAttribute("sortColumn")) || !pageParam.equals(session.getAttribute("pageParam"))) {
            session.setAttribute("sortColumn", sortColumn);
            session.setAttribute("sortOrder", EmployeeService.ASC);
        } else {
            // Dacă sortColumn și pageParam sunt aceleași, inversăm starea de sortare și actualizăm sesiunea
            sortOrder = (EmployeeService.ASC.equals(session.getAttribute("sortOrder"))) ? EmployeeService.DESC : EmployeeService.ASC;
            session.setAttribute("sortOrder", sortOrder);
        }

        // Actualizăm și sesiunea cu noul pageParam
        session.setAttribute("pageParam", pageParam);

        if (search != null && !search.isEmpty()) {
            page = employeeService.searchByKeywordIgnoreCaseAndPartialMatch(search, pageParam, sortColumn, sortOrder);
        } else {
            page = employeeService.findPage(pageParam, sortColumn, sortOrder);
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

        return "employees";
    }

    @GetMapping("/add/employee")
    public String create(Model model) {
        Employee employee = new Employee();
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        model.addAttribute("employee", employee);
        return "addemployee";
    }

    @PostMapping("/add/employee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/employee/{id}")
    public String editEmployee(@PathVariable Integer id, Model model) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return "redirect:/employees";
        }
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employee", employee);
        return "editemployee";
    }



    @PostMapping("/edit/employee/{id}")
    public String updateEmployee(@PathVariable Integer id, @ModelAttribute("employee") Employee employee) {
        employeeService.update(id, employee);
        return "redirect:/employees";
    }

    @GetMapping("/delete/employee/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "deleteemployee";
    }

    @PostMapping("/delete/employee/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }


}

