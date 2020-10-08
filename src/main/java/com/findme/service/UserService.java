package com.findme.service;

import com.findme.config.BadRequestException;
import com.findme.dao.UserDAO;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Log4j
@Transactional
@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public User save(User user) throws BadRequestException {
        validateEmailAndPhone(user.getEmail(), user.getPhone());
        user.setDateRegistered(new Date());
        return userDAO.save(user);
    }

    public User update(User user) throws BadRequestException {

        User currentUser = userDAO.findById(user.getId());
        currentUser.setLastName(user.getFirstName());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setPhone(user.getPhone());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setCountry(user.getCountry());
        currentUser.setCity(user.getCity());
        currentUser.setAge(user.getAge());
        currentUser.setReligion(user.getReligion());
        currentUser.setSchool(user.getSchool());
        currentUser.setUniversity(user.getUniversity());
        return userDAO.update(currentUser);
    }

    public void delete(Long id) throws BadRequestException {
        User user = userDAO.findById(id);
        userDAO.delete(id);
    }

    public User findById(Long id) throws BadRequestException {
        return userDAO.findById(id);
    }

    private void validateEmailAndPhone(String email, String phone) throws BadRequestException {
        if (userDAO.getUserByEmailOrPhone(email, phone) != null) {
            throw new BadRequestException("There is already registered user with this email or phone");
        }
    }
}
