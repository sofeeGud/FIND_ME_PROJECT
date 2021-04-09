package com.findme.restController;

import com.findme.dao.RelationshipDAO;
import com.findme.exceptions.BadRequestException;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Log4j
@Controller
public class UserRestController {
    private UserService userService;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public UserRestController(UserService userService, RelationshipDAO relationshipDAO) {
        this.userService = userService;
        this.relationshipDAO = relationshipDAO;

    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<String> register(@ModelAttribute User user) {
        userService.save(user);
        log.info("User with id:" + user.getId() + " was registered.");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpSession session, HttpServletRequest request) {
        User user = userService.authorization(request.getParameter("email"), request.getParameter("password"));
        log.info("User with id:" + user.getId() + " logged in");
        session.setAttribute("loggedUser", user);
        session.setAttribute("loggedUserId", String.valueOf(user.getId()));
        return new ResponseEntity<>("redirect:/users/" + user.getId(), HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.getAttribute("loggedUserId");
        log.info("User with id:"+session.getAttribute("loggedUserId")+" logged out");
        session.invalidate();
        return "redirect:/";
    }
}
