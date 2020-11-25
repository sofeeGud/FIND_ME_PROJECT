package com.findme.service;

import com.findme.exceptions.BadRequestException;
import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exceptions.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
public class RelationshipService {
    private RelationshipDAO relationshipDAO;
    private UserDAO userDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO, UserDAO userDAO) {
        this.relationshipDAO = relationshipDAO;
        this.userDAO = userDAO;
    }

    public void saveRelationship(HttpSession session, String userFromId, String userToId) throws BadRequestException {
        if (userFromId != null && userToId != null) {
            String status = userDAO.getStatusRelationship(userFromId, userToId);

            if (userFromId.equals(userToId))
                throw new BadRequestException("You cannot add yourself as a friend");
            else if (!(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userFromId)))
                throw new BadRequestException("To add friends under this user you need to log in");
            if (status == null)
                relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
            else if (status.equals(String.valueOf(RelationshipStatus.REQUESTED))) {
                throw new BadRequestException("Request to this user has already been sent");
            }
            relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
        }
    }

    public void updateRelationship(HttpSession session, String userFromId, String userToId, String status) throws InternalServerError, BadRequestException {

        if (userFromId != null && userToId != null && status != null) {
            String statusRelationship = userDAO.getStatusRelationship(userFromId, userToId);

            if (!(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userFromId))
                    && !(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userToId)))
                throw new BadRequestException("You can't use this function. You need log in");
            else if (statusRelationship == null) {
                throw new BadRequestException("You cannot update relationship which does not exist");
            } else if ((String.valueOf(((User) session.getAttribute("user")).getId()).equals(userFromId))
                    && !(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userToId))) {
                if (!(statusRelationship.equals(String.valueOf(RelationshipStatus.REQUESTED))))
                    throw new BadRequestException("You cannot update this relationship. You did not receive a request from this user or request already accepted");
            } else if (!(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userFromId))
                    && (String.valueOf(((User) session.getAttribute("user")).getId()).equals(userToId))) {
                if ((!(statusRelationship.equals(String.valueOf(RelationshipStatus.REQUESTED)))))
                    throw new BadRequestException("You cannot update this relationship. You did not receive a request from this user");

            } else if (!(String.valueOf(((User) session.getAttribute("user")).getId()).equals(userFromId))
                    && (String.valueOf(((User) session.getAttribute("user")).getId()).equals(userToId))) {
                if (((statusRelationship.equals(String.valueOf(RelationshipStatus.FRIENDS)))))
                    throw new BadRequestException("You cannot update this relationship. You already friends");

            }
            Relationship currentRelationship = relationshipDAO.getRelationship(userFromId, userToId);
            relationshipDAO.updateRelationship(
                    currentRelationship.getUserFrom().getId(), currentRelationship.getUserTo().getId(),
                    Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.valueOf(status));
        }
    }
}
