package com.findme.dao;

import com.findme.exceptions.InternalServerError;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Log4j
@Transactional
@Repository
public class UserDAO extends GeneralDAOImpl<User> {
    private static final String SQL_GET_USER_BY_EMAIL_OR_PHONE = "SELECT u FROM User u WHERE email = :email OR phone = :phone";
    private static final String SQL_GET_USER_BY_AUTH = "SELECT u FROM User u WHERE email = :email AND password = :password";
    private static final String SQL_GET_RELATIONSHIP = "SELECT r.status FROM Relationship r WHERE r.userFrom.id = :userOne and r.userTo.id = :userTwo";

    public UserDAO() {
        setClazz(User.class);
    }


    public User getUserByEmailOrPhone(String email, String phone) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_USER_BY_EMAIL_OR_PHONE, User.class)
                    .setParameter("email", email)
                    .setParameter("phone", phone)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerError(e.getMessage());
        }
    }

    public User getUserByAuthorization(String email, String password) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_USER_BY_AUTH, User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerError(e.getMessage());
        }
    }


    public String getStatusRelationship(String userIdFrom, String userIdTo) throws InternalServerError {
        try {
            return entityManager.createQuery(SQL_GET_RELATIONSHIP, User.class)
                    .setParameter("userOne", userIdFrom)
                    .setParameter("userTwo", userIdTo).getSingleResult().toString();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerError(e.getMessage());
        }
    }
}