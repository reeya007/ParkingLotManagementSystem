package com.parkinglot.controllers;

import com.parkinglot.models.User;

public interface UserAwareController {

    void setCurrentUser(User user);
}
