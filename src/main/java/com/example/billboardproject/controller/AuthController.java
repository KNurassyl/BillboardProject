package com.example.billboardproject.controller;

import com.example.billboardproject.model.User;
import com.example.billboardproject.security.SecurityConfig;
import com.example.billboardproject.service.impl.RoleServiceImpl;
import com.example.billboardproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private SecurityConfig securityConfig;

    @GetMapping(value = "/")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping(value = "/forget")
    public String forgetPage() {
        return "forget";
    }

    @PostMapping(value = "/register")
    public String registerPage(@RequestParam(name = "reg_email") String email,
                               @RequestParam(name = "reg_password") String password,
                               @RequestParam(name = "reg_confPassword") String confPassword,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "surname") String surname) {
        String redirect = "";

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setRoles(roleService.getRoleByRoleName("USER"));
        user.setPassword(securityConfig.passwordEncoder().encode(password));

        if (password.equals(confPassword)) {
            if (userService.addUser(user)) {
                redirect = "redirect:/auth/?afterRegistration";
            } else {
                redirect = "redirect:/auth/?accountExist";
            }
        } else {
            redirect = "redirect:/auth/register/?notEqualPasswords";
        }

        return redirect;
    }

}
