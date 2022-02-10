package com.noti.api.notice.dto;

import com.noti.document.Reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

	private long replyId;
	private long userId;
	private String replycontent;
	private String createDate;
	
	public ReplyDto(Reply reply) {
		this.replyId = reply.getReplyId();
		this.userId = reply.getUserId();
		this.replycontent = reply.getReplycontent();
		this.createDate = reply.getCreateDate();
	}
	
	public Reply toEntity() {
		return Reply.builder()
				.replyId(this.replyId)
				.userId(this.userId)
				.replycontent(this.replycontent)
				.createDate(this.createDate)
				.build();
	}
}
