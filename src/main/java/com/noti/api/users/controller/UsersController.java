package com.noti.api.users.controller;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noti.api.user.dto.UserDto;
import com.noti.api.user.service.UserService;
import com.noti.api.users.dto.UserSearch;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("users")
@RestController
public class UsersController {

	UserService service;
	
	@GetMapping("")
	@ResponseBody
	public ResponseEntity<List<UserDto>> getUsersInfo(UserSearch userSearch) {
		return ResponseEntity.ok().body(
			service.getUsersInfo(userSearch)
		);
	}
	
	@GetMapping("/{userId}")
	@ResponseBody
	public ResponseEntity<UserDto> userUpdate(@PathVariable(name = "userId", required = true) long userId) throws AuthenticationException {
		return ResponseEntity.ok().body(
			service.getUserInfo(userId)
		);
	}
}