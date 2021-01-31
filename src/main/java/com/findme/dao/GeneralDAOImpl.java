package com.findme.dao;


import com.findme.exceptions.BadRequestException;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Log4j
public abstract class GeneralDAOImpl<T> implements GeneralDAO<T> {
    private Class<T> clazz;

    @PersistenceContext
    public EntityManager entityManager;

    final void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Transactional
    @Override
    public T save(T t) throws BadRequestException {
        try {
            entityManager.persist(t);
            log.info("New " + clazz.getSimpleName() + " saved");
            return t;
        } catch (Exception e) {
            throw new BadRequestException("Save : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T update(T t) throws BadRequestException {
        try {
            entityManager.merge(t);
            log.info("Entity " + clazz.getSimpleName() + " was updated");
            return t;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("Update : " + t + " failed" + e.getMessage());
        }
    }

    @Override
    public T findById(Long id) throws BadRequestException {
        try {
            return entityManager.find(clazz, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("Find : " + id + " failed" + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws BadRequestException {
        try {
            entityManager.remove(findById(id));
            log.info("Entity " + clazz.getSimpleName() + " with id: " + id + " was deleted");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("Delete : " + id + " failed" + e.getMessage());
        }
    }
}
