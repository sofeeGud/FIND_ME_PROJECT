package com.findme.service;

import com.findme.exceptions.BadRequestException;
import com.findme.dao.UserDAO;
import com.findme.exceptions.InternalServerError;
import com.findme.exceptions.NotFoundException;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public User save(User user) throws InternalServerError, BadRequestException {
        if (userDAO.getUserByEmailOrPhone(user.getEmail(), user.getPhone()) != null) {
            throw new BadRequestException("There is already registered user with this email or phone");
        }
        user.setDateRegistered(new Date());
        return userDAO.save(user);
    }

    public User update(User user) throws InternalServerError, BadRequestException {

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

    public void delete(Long id) throws InternalServerError {
        User user = userDAO.findById(id);
        userDAO.delete(id);
    }

    public User findById(Long id) throws BadRequestException, NotFoundException {
        return userDAO.findById(id);
    }

    public User authorization(String email, String password) throws InternalServerError, BadRequestException {
        User user = userDAO.getUserByAuthorization(email, password);
        if (user == null)
            throw  new BadRequestException("User with email:" +email+" and password: *****  not found");
        return user;
    }

}
