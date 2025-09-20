package com.javaweb.service;

import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;

public interface UserService {
	void register(UserDTO userDTO) throws Exception;
	String loginUser(UserLoginDTO userLogin) throws Exception;
}
