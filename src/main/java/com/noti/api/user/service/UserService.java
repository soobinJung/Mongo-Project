package com.noti.api.user.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.noti.api.user.dto.UserDto;
import com.noti.document.User;
import com.noti.util.DateProcess;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	MongoTemplate template;
	DateProcess date;
	
	static final String DATEFORMAT = "yyyy/MM/dd";
	static String userEmail = "";
	
	/**
	 * 사용자 정보
	 */
	public UserDto getUsaerInfo() {
		
		if( userEmail.equals("") ) {
			return UserDto.builder()
					      .loginState(false)
					      .build();
		}
		
		return new UserDto(
			template.findOne(new Query(new Criteria("userEmail").is(userEmail))
			, User.class)
		);
		 
	}
	
	/**
	 * 로그인
	 */
	public UserDto signIn( String userEmail, String userPwd ) {
		
		if(userEmail.equals("") || userPwd.equals("") || userEmail == null || userPwd == null ) {
			return UserDto.builder().loginState(false).stateMsg("Null Value").build();
		}
		
		Query qurey = new Query();
		
		qurey.addCriteria( Criteria.where("userEmail").is(userEmail));
		qurey.addCriteria( Criteria.where("userPwd").is(userPwd));
		
		UserDto userDto = new UserDto(template.findOne(qurey, User.class));
		
		userDto.setLoginState( userDto.getUserId() == 0 ? false : true );
		
		if(userDto.getUserId() == 0) {
			userDto.setLoginState( false );
		}else {
			this.userEmail = userDto.getUserEmail();
			userDto.setLoginState( true );
		}
		return userDto;
	}
	
	/**
	 * 회원 가입
	 */
	public UserDto signUp(UserDto userDto) {
		
		if(new UserDto(template.findOne(new Query().addCriteria( Criteria.where("userEmail").is(userDto.getUserEmail())), User.class)).getUserId() != 0) {
			return UserDto.builder().loginState(false).stateMsg("Overlap Email").build();
		};
		
		long userId = template.count(new Query(), User.class) + 1;
		
		// 유니크 키 생성
		while ( userId > 0) {
			if(new UserDto(template.findOne(new Query(new Criteria("_id").is(++userId)), User.class)).getUserId() == 0) {
				break;
			} 
		} 
		userDto.setUserId(userId);
		userDto.setCreateDate(date.getTodayDate(DATEFORMAT));
		userDto.setUpdateDate(date.getTodayDate(DATEFORMAT));
		
		return new UserDto(template.insert(userDto.toEntity()));
	}
	
	
	/**
	 * 로그 아웃
	 */
	public UserDto signOut() {
		
		this.userEmail = "";
		return UserDto.builder().stateMsg("logout success").build();
	}
}
