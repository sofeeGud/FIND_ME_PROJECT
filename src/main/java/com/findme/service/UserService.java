package com.findme.service;

import com.findme.config.BadRequestException;
import com.findme.dao.UserDAO;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public User save(User user) throws BadRequestException {
        return userDAO.save(user);
    }

    public User update(User user) throws BadRequestException {
        return userDAO.update(user);
    }

    public void delete(Long id) throws BadRequestException {
        User user = userDAO.findById(id);
        userDAO.delete(id);
    }

    public User findById(Long id) throws BadRequestException {
        return userDAO.findById(id);
    }
}
