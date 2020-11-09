package com.findme.service;

import com.findme.config.BadRequestException;
import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
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

    public RelationshipStatus getRelationshipStatus(String userFromId, String userToId) throws BadRequestException {
//        validateIncomingParams(userFromId, userToId, null);
        Relationship relationship = relationshipDAO.getRelationship(userFromId, userToId);
        return relationship == null ? null : relationship.getStatus();
    }
    public void saveRelationship(String userFromId, String userToId) throws BadRequestException{
        if (userFromId != null && userToId != null)
        relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
    }

    public void updateRelationship(String userFromId, String userToId, String status) throws BadRequestException{
        Relationship currentRelationship = relationshipDAO.getRelationship(userFromId, userToId);
 //       validateRelationshipUpdate(currentRelationship, userFromId, userFromId, status);
        relationshipDAO.updateRelationship(
                currentRelationship.getUserFrom().getId(), currentRelationship.getUserTo().getId(),
                Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.valueOf(status));
    }
}
