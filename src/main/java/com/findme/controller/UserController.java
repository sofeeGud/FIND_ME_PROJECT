package com.findme.controller;

import com.findme.config.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @RequestMapping(path = "/register-user", method = RequestMethod.POST)
    public ResponseEntity<String> register(@ModelAttribute User user) {
        try {
           userService.save(user);
            return new ResponseEntity<>("ok", HttpStatus.OK);

        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
