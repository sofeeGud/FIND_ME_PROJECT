package com.findme.dao;

import com.findme.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDAO extends GeneralDAOImpl<User> {

    @PersistenceContext
    private EntityManager entityManager;
}
