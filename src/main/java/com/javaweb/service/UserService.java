package com.javaweb.service;

import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;

public interface UserService {
	public void register(UserDTO userDTO) throws Exception;
	public String loginUser(UserLoginDTO userLogin) throws Exception;
}
