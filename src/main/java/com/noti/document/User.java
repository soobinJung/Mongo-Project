package com.noti.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "USER")
public class User {
	
	@Id
	private long _id;
	
	private String userEmail;
	private String userName;
	private String userPwd;
	private String userAdminLevel;
	private String createDate;
	private String updateDate;
}
