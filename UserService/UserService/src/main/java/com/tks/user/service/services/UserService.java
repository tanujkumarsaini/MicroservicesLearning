package com.tks.user.service.services;

import com.tks.user.service.entities.User;

import java.util.List;

public interface UserService {
    //create
    User saveUser(User user);

    //get All User
    List<User> getAllUser();

    //get single user of given userId
    User getUser(String userId);

    //TODO: delete
    //TODO: update
}
