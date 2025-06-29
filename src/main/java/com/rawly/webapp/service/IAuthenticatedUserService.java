package com.rawly.webapp.service;

import com.rawly.webapp.security.IAuthenticatedUser;

public interface IAuthenticatedUserService {

    IAuthenticatedUser loadUserByUserEmail(String email);
}
