package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.models.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

//@Transactional
@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public User save(User user) throws Exception {
        return userDAO.save(user);
    }

    public User update(User user) throws Exception {
        return userDAO.update(user);
    }

    public void delete(Long id) throws Exception {
        User user = userDAO.findById(id);
        userDAO.delete(id);
    }

    public User findById(Long id) throws HttpServerErrorException.InternalServerError, NotFoundException {
        return userDAO.findById(id);
    }
}
