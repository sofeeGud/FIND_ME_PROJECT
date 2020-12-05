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


@Service
public class RelationshipService {
    private RelationshipDAO relationshipDAO;
    private UserDAO userDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO, UserDAO userDAO) {
        this.relationshipDAO = relationshipDAO;
        this.userDAO = userDAO;
    }

    public void saveRelationship(String userFromId, String userToId) throws BadRequestException {
        if (userFromId != null && userToId != null) {
            validateRelationshipSave(userFromId, userToId);
            relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
        }
    }

    public void updateRelationship(String userFromId, String userToId, String status) throws InternalServerError, BadRequestException {
        if (userFromId != null && userToId != null && status != null) {
            Relationship currentRelationship = relationshipDAO.getRelationship(userFromId, userToId);
            validateRelationshipUpdate(currentRelationship, userFromId, userToId, status);
            relationshipDAO.updateRelationship(
                    currentRelationship.getUserFrom().getId(), currentRelationship.getUserTo().getId(),
                    Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.valueOf(status));
        }
    }
    private void validateRelationshipUpdate(Relationship currentRelationship, String userFromId, String userToId, String status) throws BadRequestException {

        User userTo = userDAO.findById(Long.valueOf(userToId));
        if (userTo == null || currentRelationship == null)
            throw new BadRequestException("Relationship from user " + userFromId + " to user " + userToId + " save - failed. Wrong data.");
        if (userFromId.equals(userToId))
            throw new BadRequestException("You cannot send a request to yourself");
        if (currentRelationship != null && status != null) {
            if (currentRelationship.equals(RelationshipStatus.FRIENDS) && status.equals(RelationshipStatus.FRIENDS))
                throw new BadRequestException("You and user " + userTo.getFirstName() + " are already friends");
            if (currentRelationship.equals(RelationshipStatus.DELETED) && status.equals(RelationshipStatus.FRIENDS))
                throw new BadRequestException("You cannot delete a user who is not your friend yet");
            if (currentRelationship.equals(RelationshipStatus.REQUESTED) && status.equals(RelationshipStatus.DELETED))
                throw new BadRequestException("To remove a user " + userTo.getFirstName() + " from friends, first accept the request");
            if (currentRelationship.equals(RelationshipStatus.CANCELED) && status.equals(RelationshipStatus.FRIENDS))
                throw new BadRequestException("User " + userTo.getFirstName() + " canceled the friend request");
            if (currentRelationship.equals(RelationshipStatus.REJECTED) && status.equals(RelationshipStatus.FRIENDS))
                throw new BadRequestException("The friend request has already been rejected");
            if (currentRelationship.equals(RelationshipStatus.DELETED) && status.equals(RelationshipStatus.DELETED))
                throw new BadRequestException("User " + userTo.getFirstName() + " has already been removed from the friends list");
        }
    }

    private void validateRelationshipSave(String userFromId, String userToId) throws BadRequestException{

        User userTo = userDAO.findById(Long.valueOf(userToId));
        if (userFromId.equals(userToId))
            throw new BadRequestException("You cannot send a request to yourself");
        if (relationshipDAO.getRelationship(userFromId, userToId) != null && relationshipDAO.getRelationship(userFromId, userToId).equals(RelationshipStatus.FRIENDS))
            throw new BadRequestException("You and user " + userTo.getFirstName() + " are already friends");
    }
}
