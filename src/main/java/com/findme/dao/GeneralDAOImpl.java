package com.findme.dao;


import com.findme.config.BadRequestException;
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
    public T save(T t) throws BadRequestException {
        try {
            entityManager.persist(t);
            return t;
        } catch (Exception e) {
            throw new BadRequestException("Save : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T update(T t) throws BadRequestException {
        try {
            entityManager.merge(t);
            return t;
        } catch (Exception e) {
            throw new BadRequestException("Update : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T findById(Long id) throws BadRequestException {
        try {
            return entityManager.find(clazz, id);
        } catch (Exception e) {
            throw new BadRequestException("Find : " + id + " failed" + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws BadRequestException {
        try {
            entityManager.remove(findById(id));
        } catch (Exception e) {
            throw new BadRequestException("Delete : " + id + " failed" + e.getMessage());
        }
    }
}
