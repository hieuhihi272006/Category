package com.javaweb.service;

import java.util.List;

import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.CartResponse;
import com.javaweb.model.response.InformationUserResponse;
import com.javaweb.model.response.OrderBuyResponse;

public interface UserService {
	public void register(UserDTO userDTO) throws Exception;
	public String loginUser(UserLoginDTO userLogin) throws Exception;
	InformationUserResponse getProfile(UserEntity user);
	List<OrderBuyResponse> getOrder(UserEntity user);
	List<CartResponse> getCard(UserEntity user);
	void deleteCart(Long id);
}
