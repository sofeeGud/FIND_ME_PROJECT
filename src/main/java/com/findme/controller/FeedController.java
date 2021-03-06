package com.findme.controller;

import com.findme.exceptions.BadRequestException;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Log4j
@Controller
public class FeedController {
    @RequestMapping(path = "/feed", method = RequestMethod.GET)
    public String feed(HttpSession session, Model model) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            model.addAttribute("error", new BadRequestException("You are not logged in to see this information."));
            return "errors/forbidden";

        }
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "feed";
    }
}
