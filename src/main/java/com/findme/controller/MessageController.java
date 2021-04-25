package com.findme.controller;

import com.findme.dao.MessageDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exceptions.BadRequestException;
import com.findme.models.Dialogs;
import com.findme.models.Message;
import com.findme.service.MessageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Log4j
@Controller
public class MessageController {
    private MessageService messageService;
    private RelationshipDAO relationshipDAO;
    private MessageDAO messageDAO;

    @Autowired
    public MessageController(MessageService messageService, RelationshipDAO relationshipDAO, MessageDAO messageDAO) {
        this.messageService = messageService;
        this.relationshipDAO = relationshipDAO;
        this.messageDAO = messageDAO;
    }


    @RequestMapping(path = "/messages/{userId}", method = RequestMethod.GET)
    public String messagesByUser(HttpSession session, Model model, @PathVariable String userId) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            model.addAttribute("error", new BadRequestException("You are not logged in to see this information."));
            return "errors/forbidden";
        }

        model.addAttribute("dialogsList", relationshipDAO.getFriendsList(loggedUserId));
        model.addAttribute("messagesList", messageService.getMessageList(loggedUserId, userId));
        model.addAttribute("loggedUserId", loggedUserId);
        model.addAttribute("recipientId", userId);

        return "messages";
    }

    @RequestMapping(path = "/messages", method = RequestMethod.GET)
    public String messages(HttpSession session, Model model) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            model.addAttribute("error", new BadRequestException("You are not logged in to see this information."));
            return "errors/forbidden";
        }

        model.addAttribute("friendsList", relationshipDAO.getFriendsList(loggedUserId));
        model.addAttribute("loggedUserId", loggedUserId);

        return "messages";
    }
}
