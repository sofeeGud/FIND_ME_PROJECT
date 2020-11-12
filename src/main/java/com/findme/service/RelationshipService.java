package com.findme.service;

import com.findme.exceptions.BadRequestException;
import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exceptions.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
        if (userFromId != null && userToId != null)
            relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
    }

    public void updateRelationship(String userFromId, String userToId, String status) throws InternalServerError, BadRequestException {
        if (userFromId != null && userToId != null && status != null) {
            Relationship currentRelationship = relationshipDAO.getRelationship(userFromId, userToId);
            relationshipDAO.updateRelationship(
                    currentRelationship.getUserFrom().getId(), currentRelationship.getUserTo().getId(),
                    Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.valueOf(status));
        }
    }
}
