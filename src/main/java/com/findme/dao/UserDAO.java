package com.findme.dao;

import com.findme.exceptions.InternalServerError;
import com.findme.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;


@Repository
public class UserDAO extends GeneralDAOImpl<User> {
    private static final String SQL_GET_USER_BY_EMAIL_OR_PHONE = "SELECT u FROM User u WHERE email = :email OR phone = :phone";
    private static final String SQL_GET_USER_BY_AUTH = "SELECT u FROM User u WHERE email = :email AND password = :password";

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
            throw new InternalServerError(e.getMessage());
        }
    }
}