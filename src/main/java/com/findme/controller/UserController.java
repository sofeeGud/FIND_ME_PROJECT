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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable String id) {

        try {
            String loggedUserId = (String) session.getAttribute("loggedUserId");
            if(loggedUserId==null) {
                model.addAttribute("error", new BadRequestException("You are not logged in to see this information."));
                throw  new BadRequestException("User is not authorized");
            }
            User user = userService.findById(Long.parseLong(id));
            if(loggedUserId.equals(id)){
                model.addAttribute("user", user);
                model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
                return "profile";
            }
            return "404";

        } catch (BadRequestException e) {
            return "400";

        } catch (HttpServerErrorException.InternalServerError e) {
            return "500";
        }
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
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


    @RequestMapping(path = "/authorization", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpSession session, HttpServletRequest request) {
        try {
            User user = userService.authorization(request.getParameter("email"), request.getParameter("password"));
            session.setAttribute("loggedUser", user);
            session.setAttribute("loggedUserId", String.valueOf(user.getId()));
            return new ResponseEntity<>("redirect:/user/" + user.getId(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.getAttribute("loggedUserId");
        session.invalidate();
        return "redirect:/";
    }
}
