package com.findme.controller;

import com.findme.config.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String id) {
        try {
            User user = userService.findById(Long.parseLong(id));
            if (user != null) {
                model.addAttribute("user", user);
                return "profile";
            }
            return "404";

        } catch (BadRequestException e) {
            return "400";

        } catch (HttpServerErrorException.InternalServerError e) {
            return "500";
        }
    }
}
