package com.findme.dao;


import com.findme.models.Post;
import javassist.NotFoundException;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GeneralDAOImpl<T> implements GeneralDAO<T> {
    private Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    final void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public T save(T t) throws Exception {
        try {
            entityManager.persist(t);
            return t;
        } catch (Exception e) {
            throw new Exception("Save : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T update(T t) throws Exception {
        try {
            entityManager.merge(t);
            return t;
        } catch (Exception e) {
            throw new Exception("Update : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T findById(Long id) throws HttpServerErrorException.InternalServerError, NotFoundException {
            return entityManager.find(clazz, id);
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            entityManager.remove(findById(id));
        } catch (Exception e) {
            throw new Exception("Delete : " + id + " failed" + e.getMessage());
        }
    }
}
