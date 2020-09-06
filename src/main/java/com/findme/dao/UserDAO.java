package com.findme.dao;

import com.findme.models.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserDAO extends GeneralDAOImpl<User> {

    public UserDAO() {
        setClazz(User.class);
    }
}
