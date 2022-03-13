package com.noti.document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Reply {
	
	private long replyId;
	private long userId;
	private String replycontent;
	private String createDate;
	private String updateDate;
}