package com.findme.service;

import com.findme.exceptions.BadRequestException;
import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exceptions.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.validation.*;
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
        validateRelationshipSave(userFromId, userToId);
        relationshipDAO.saveRelationship(Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.REQUESTED);
    }

    public void updateRelationship(String userFromId, String userToId, String status) throws InternalServerError, BadRequestException {
        Relationship currentRelationship = relationshipDAO.getRelationship(userFromId, userToId);
        validateRelationshipUpdate(currentRelationship, userFromId, userToId, status);
        relationshipDAO.updateRelationship(
                currentRelationship.getUserFrom().getId(), currentRelationship.getUserTo().getId(),
                Long.valueOf(userFromId), Long.valueOf(userToId), RelationshipStatus.valueOf(status));
    }

    private void validateRelationshipUpdate(Relationship currentRelationship, String userFromId, String userToId, String status) throws BadRequestException, InternalServerError {
        if (userFromId != null && userToId != null && status != null) {

            User userTo = userDAO.findById(Long.valueOf(userToId));
            if (userTo == null || currentRelationship == null)
                throw new BadRequestException("Relationship from user " + userFromId + " to user " + userToId + " save - failed. Wrong data.");
            if (userFromId.equals(userToId))
                throw new BadRequestException("It is not possible to send a request to yourself");

            AbstractRelationshipValidator friendsVal = new FriendsStatusValidator();
            AbstractRelationshipValidator canceledVal = new CanceledStatusValidator();
            AbstractRelationshipValidator deletedVal = new DeletedStatusValidator();
            AbstractRelationshipValidator rejectedVal = new RejectedStatusValidator();
            AbstractRelationshipValidator requestedVal = new RequestedStatusValidator();

            friendsVal.setNextAbstractChainValidator(canceledVal);
            canceledVal.setNextAbstractChainValidator(deletedVal);
            deletedVal.setNextAbstractChainValidator(rejectedVal);
            rejectedVal.setNextAbstractChainValidator(requestedVal);

            friendsVal.check(RelationshipValidatorParams.builder()
                    .oldStatus(currentRelationship.getStatus())
                    .newStatus(RelationshipStatus.valueOf(status))
                    .relationshipDateModified(currentRelationship.getDateModified())
                    .friendsCnt(relationshipDAO.getFriendsCount(userFromId))
                    .outgoingReqCnt(relationshipDAO.getOutgoingRequestsCount(userFromId))
                    .userFromId(userFromId)
                    .userToId(userToId)
                    .build());
        }
    }

    private void validateRelationshipSave(String userFromId, String userToId) throws BadRequestException, InternalServerError {
        if (userFromId != null && userToId != null) {

            if (relationshipDAO.getRelationship(userFromId, userToId) != null)
                throw new BadRequestException("Relationship from user " + userFromId + " to user " + userToId + " save - failed. There is an active relationship");
            if (userFromId.equals(userToId))
                throw new BadRequestException("It is not possible to send a request to yourself");

            AbstractRelationshipValidator requestedVal = new RequestedStatusValidator();
            requestedVal.check(RelationshipValidatorParams.builder()
                    .oldStatus(RelationshipStatus.DELETED)
                    .newStatus(RelationshipStatus.REQUESTED)
                    .friendsCnt(relationshipDAO.getFriendsCount(userFromId))
                    .outgoingReqCnt(relationshipDAO.getOutgoingRequestsCount(userFromId))
                    .userFromId(userFromId)
                    .userToId(userToId)
                    .build());
        }
    }

}
