package com.noti.api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noti.api.user.dto.UserDto;
import com.noti.api.user.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {
	UserService service;
	
	@GetMapping("")
	@ResponseBody
	public ResponseEntity<UserDto> getUserInfo( ) {
		return ResponseEntity.ok().body(
			service.getUsaerInfo()
		);
	}
	
	@PostMapping("/sing-in")
	@ResponseBody
	public ResponseEntity<UserDto> signIn( String userEmail, String userPwd ) {
		return ResponseEntity.ok().body(
			service.signIn(userEmail, userPwd)
		);
	}
	
	@PostMapping("/sign-up")
	@ResponseBody
	public ResponseEntity<UserDto> signUp(UserDto userDto) {
		return ResponseEntity.ok().body(
			service.signUp(userDto)
		);
	}
	
	@PostMapping("/sign-out")
	@ResponseBody
	public ResponseEntity<UserDto> signOut( ) {
		return ResponseEntity.ok().body(
			service.signOut()
		);
	}
}
