package com.findme.controller;

import com.findme.exceptions.BadRequestException;
import com.findme.dao.RelationshipDAO;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class UserController {
    private UserService userService;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public UserController(UserService userService, RelationshipDAO relationshipDAO) {
        this.userService = userService;
        this.relationshipDAO = relationshipDAO;

    }

    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable String userId) {

            String loggedUserId = (String) session.getAttribute("loggedUserId");
        if(loggedUserId==null) {
            model.addAttribute("error", new BadRequestException("You are not logged in to see this information."));
            return "errors/badRequest";
        }
            User user = userService.findById(Long.valueOf(userId));
            if (user !=null){
                model.addAttribute("user", user);
                model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
            Relationship rel = relationshipDAO.getRelationship(loggedUserId, userId);
            model.addAttribute("btnViewProp", Objects.requireNonNull(getButtonsViewProperty(userId, rel)));
            model.addAttribute("friendsList", relationshipDAO.getFriendsList(userId));
            model.addAttribute("friendsCount", relationshipDAO.getFriendsCount(userId));
            if(rel != null)
                model.addAttribute("relStatus", rel.getStatus());
            if(loggedUserId.equals(userId)){
                model.addAttribute("incomingRequests", relationshipDAO.getIncomingRequests(loggedUserId));
                model.addAttribute("outgoingRequests", relationshipDAO.getOutgoingRequests(loggedUserId));
            }
                return "profile";
            }
            return "errors/notFound";
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<String> register(@ModelAttribute User user) {
            userService.save(user);
            return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpSession session, HttpServletRequest request) {
        User user = userService.authorization(request.getParameter("email"), request.getParameter("password"));
        if (user == null)
           return new ResponseEntity<>("User with email:" +request.getParameter("email")+" and password: *****  not found",HttpStatus.BAD_REQUEST);
        session.setAttribute("loggedUser", user);
        session.setAttribute("loggedUserId", String.valueOf(user.getId()));
        return new ResponseEntity<>("redirect:/users/" + user.getId(), HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.getAttribute("loggedUserId");
        session.invalidate();
        return "redirect:/";
    }

    private String getButtonsViewProperty(String userToId, Relationship rel) throws BadRequestException{
        Long userToIdL;
        try {
            userToIdL = Long.valueOf(userToId);
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage());
        }
        if(rel == null)
            return "btn-addSave";

        if(rel.getStatus() == RelationshipStatus.FRIENDS)
            return "btn-remove";

        if(rel.getStatus()==RelationshipStatus.REQUESTED && rel.getUserTo().getId().equals(userToIdL))
            return "btn-sent";

        if(rel.getStatus()==RelationshipStatus.REJECTED && rel.getUserFrom().getId().equals(userToIdL))
            return "btn-rejected";

        if(rel.getStatus() != RelationshipStatus.REQUESTED && rel.getStatus() != RelationshipStatus.FRIENDS)
            return "btn-addUpd";

        if(rel.getStatus() == RelationshipStatus.REQUESTED && rel.getUserFrom().getId().equals(userToIdL))
            return "btn-accept";

        return null;
    }
}
