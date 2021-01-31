package com.findme.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
@Log4j
@Controller
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session){
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if(loggedUserId != null) {
            log.warn("User is not authorized");
            return "redirect:/users/" + loggedUserId;
        }
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "index";
    }
}
