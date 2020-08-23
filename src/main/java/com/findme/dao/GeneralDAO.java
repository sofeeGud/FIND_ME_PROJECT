package com.findme.dao;

public interface GeneralDAO<T> {

    T save(T t) throws Exception;
    T update(T t) throws  Exception;
    T findById(Long id) throws Exception;
    void delete(Long id) throws Exception;
}
