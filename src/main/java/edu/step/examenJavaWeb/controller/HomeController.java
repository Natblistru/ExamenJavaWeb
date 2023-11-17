package edu.step.examenJavaWeb.controller;

import edu.step.examenJavaWeb.model.User;
import edu.step.examenJavaWeb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/")
    public String index(Model model) {
        //Inscriptia "Bun venit, Nume Prenume"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        return "index";
    }
}

