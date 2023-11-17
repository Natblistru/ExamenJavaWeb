package edu.step.examenJavaWeb.controller;


import edu.step.examenJavaWeb.model.Company;
import edu.step.examenJavaWeb.model.User;
import edu.step.examenJavaWeb.service.CompanyService;
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
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/companies")
    public String index(
            @RequestParam(value = "p", defaultValue = "1") Integer pageParam,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        Page<Company> page;

        if (search != null && !search.isEmpty()) {
            page = companyService.searchByKeywordIgnoreCaseAndPartialMatch(search, pageParam);
        } else {
            page = companyService.findPage(pageParam);
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
        return "companies";
    }

    @GetMapping("/add/company")
    public String createCompany(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "addcompany";
    }

    @PostMapping("/add/company")
    public String saveCompany(@ModelAttribute("company") Company company) {
        companyService.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/edit/company/{id}")
    public String editCompany(@PathVariable Integer id, Model model) {
        Company company = companyService.findById(id);
        if (company == null) {
            return "redirect:/companies";
        }
        model.addAttribute("company", company);
        return "editcompany";
    }

    @PostMapping("/edit/company/{id}")
    public String updateCompany(@PathVariable Integer id, @ModelAttribute("company") Company company) {
        companyService.update(id, company);
        return "redirect:/companies";
    }

    @GetMapping("/delete/company/{id}")
    public String confirmDeleteCompany(@PathVariable Integer id, Model model) {
        Company company = companyService.findById(id);
        model.addAttribute("company", company);
        return "deletecompany";
    }

    @PostMapping("/delete/company/{id}")
    public String deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
        return "redirect:/companies";
    }
}

