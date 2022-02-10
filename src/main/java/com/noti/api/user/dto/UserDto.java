package com.noti.api.user.dto;

import com.noti.document.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private long userId;
	private String userEmail;
	private String userName;
	private String userPwd;
	private String userAdminLevel;
	private String createDate;
	private String updateDate;
	private boolean loginState;
	private String stateMsg;
	
	public UserDto ( User user ) {
		if( user != null ) {
			this.userId = user.get_id (); 
			this.userEmail = user.getUserEmail();
			this.userName = user.getUserName();
			this.userPwd = user.getUserPwd();
			this.userAdminLevel = user.getUserAdminLevel();
			this.createDate = user.getCreateDate();
			this.updateDate = user.getUpdateDate();
		}else {
			this.userId = 0; 
		}
	}
	
	public User toEntity () {
		return User.builder()
				._id(this.userId)
				.userEmail(this.userEmail)
				.userName(this.userName)
				.userPwd(userPwd)
				.userAdminLevel(userAdminLevel)
				.createDate(this.createDate)
				.updateDate(this.updateDate)
				.build();
	}
}
