package com.findme.dao;

import com.findme.exceptions.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public class RelationshipDAO extends GeneralDAOImpl<Relationship> {
    private static final String SQL_ADD_NEW_RELATIONSHIP = "INSERT INTO RELATIONSHIP(USER_FROM_ID, USER_TO_ID, STATUS) VALUES (:userFromId, :userToId, :status)";
    private static final String SQL_UPDATE_RELATIONSHIP = "UPDATE RELATIONSHIP SET USER_FROM_ID = :userFromId_new, USER_TO_ID = :userToId_new, STATUS = :status WHERE USER_FROM_ID = :userFromId_old AND USER_TO_ID = :userToId_old";
    private static final String SQL_GET_RELATIONSHIP = "" +
            "SELECT r FROM Relationship r " +
            "WHERE (r.userFrom.id = :userFromId AND r.userTo.id = :userToId) " +
            "OR (r.userTo.id = :userFromId AND r.userFrom.id = :userToId)";
    private static final String SQL_GET_INCOMING_REQ = "SELECT r.userFrom FROM Relationship r WHERE r.status = 'REQUESTED' AND r.userTo.id = :userId";
    private static final String SQL_GET_OUTGOING_REQ = "SELECT r.userTo FROM Relationship r WHERE r.status = 'REQUESTED' AND r.userFrom.id = :userId";
    private static final String SQL_GET_FRIENDS = "" +
            "SELECT u" +
            " FROM User u, Relationship r" +
            " WHERE r.status = 'FRIENDS' AND ((r.userFrom.id = :userId AND r.userTo.id = u.id) OR (r.userTo.id = :userId AND r.userFrom.id = u.id))";
    private static final String SQL_GET_FRIENDS_COUNT = "SELECT COUNT(r) AS cnt FROM Relationship r WHERE r.status = 'FRIENDS' AND (r.userFrom.id = :userId OR r.userTo.id = :userId)";

    private static final String SQL_GET_OUTGOING_REQ_COUNT = "SELECT COUNT(r) FROM Relationship r WHERE r.status = 'REQUESTED' AND r.userFrom.id = :userId";

    public void saveRelationship(Long userFromId, Long userToId, RelationshipStatus status) throws InternalServerError {
        try {
            int res = entityManager.createNativeQuery(SQL_ADD_NEW_RELATIONSHIP)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .setParameter("status", status.toString())
                    .executeUpdate();

        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void updateRelationship(Long userFromId_old, Long userToId_old, Long userFromId_new, Long userToId_new, RelationshipStatus status) throws InternalServerError {
        try {
            entityManager.createNativeQuery(SQL_UPDATE_RELATIONSHIP)
                    .setParameter("userFromId_old", userFromId_old)
                    .setParameter("userToId_old", userToId_old)
                    .setParameter("userFromId_new", userFromId_new)
                    .setParameter("userToId_new", userToId_new)
                    .setParameter("status", status.toString())
                    .executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public Relationship getRelationship(String userFromId, String userToId) throws InternalServerError {
        try {
            Relationship result = null;
            List<Relationship> resultList = entityManager.createQuery(SQL_GET_RELATIONSHIP, Relationship.class)
                    .setParameter("userFromId", Long.valueOf(userFromId))
                    .setParameter("userToId", Long.valueOf(userToId))
                    .getResultList();
            if (resultList != null && resultList.size() > 0)
                result = resultList.get(0);
            return result;
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<User> getIncomingRequests(String userId) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_INCOMING_REQ, User.class)
                    .setParameter("userId", Long.valueOf(userId))
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<User> getOutgoingRequests(String userId) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_OUTGOING_REQ, User.class)
                    .setParameter("userId", Long.valueOf(userId))
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<User> getFriendsList(String userId) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_FRIENDS, User.class)
                    .setParameter("userId", Long.valueOf(userId))
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public int getFriendsCount(String userId) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_FRIENDS_COUNT, Long.class)
                    .setParameter("userId", Long.valueOf(userId))
                    .getSingleResult().intValue();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public int getOutgoingRequestsCount(String userId) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_OUTGOING_REQ_COUNT, Long.class)
                    .setParameter("userId", Long.valueOf(userId))
                    .getSingleResult().intValue();
        }catch (Exception e){
            throw new InternalServerError(e.getMessage(), e.getCause());
        }
    }

}
