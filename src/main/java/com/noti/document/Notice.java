package com.noti.document;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "NOTICE")
public class Notice {

	private long _id;
	
	private String noticeTitle;
	private String noticeContent;
	private String noticeImageFile;
	private String createDate;
	private String updateDate;
	private List<Reply> replyList;
}
