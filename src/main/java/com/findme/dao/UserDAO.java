package com.findme.dao;

import com.findme.config.BadRequestException;
import com.findme.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO extends GeneralDAOImpl<User> {
    private static final String SQL_GET_USER_BY_EMAIL_OR_PHONE = "SELECT u FROM User u WHERE email = :email OR phone = :phone";

    public UserDAO() {
        setClazz(User.class);
    }


    public User getUserByEmailOrPhone(String email, String phone) throws BadRequestException {
        try {
            User result = null;
            List<User> userList = entityManager.createQuery(SQL_GET_USER_BY_EMAIL_OR_PHONE, User.class)
                    .setParameter("email", email)
                    .setParameter("phone", phone)
                    .getResultList();
            if (userList != null && userList.size() > 0)
                result = userList.get(0);
            return result;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}