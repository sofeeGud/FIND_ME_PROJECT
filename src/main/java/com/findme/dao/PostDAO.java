package com.findme.dao;

import com.findme.models.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostDAO extends GeneralDAOImpl<Post> {


    @PersistenceContext
    private EntityManager entityManager;
}
