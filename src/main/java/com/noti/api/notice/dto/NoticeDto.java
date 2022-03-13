package com.noti.api.notice.dto;

import java.util.ArrayList;
import java.util.List;

import com.noti.api.user.dto.UserDto;
import com.noti.document.Notice;
import com.noti.document.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

	private long noticeId;
	private long userId;
	private int noticeCount;
	
	private String noticeTitle;
	private String noticeContent;
	private String noticeImageFile;
	private String createDate;
	private String updateDate;
	
	private List<ReplyDto> replyList;
	private UserDto user;

	public NoticeDto ( Notice notice ) {
		if(notice != null) {
			this.noticeId = notice.get_id ();
			this.userId   = notice.getUserId();
			this.noticeTitle = notice.getNoticeTitle();
			this.noticeContent = notice.getNoticeContent();
			this.noticeImageFile = notice.getNoticeImageFile();
			this.noticeCount = notice.getNoticeCount();
			this.createDate = notice.getCreateDate();
			this.updateDate = notice.getUpdateDate();
			this.replyList = new ArrayList<>();
			this.user = new UserDto();
			
			// 댓글 형변환 
			if(notice.getReplyList() != null && notice.getReplyList().size() > 0) {
				notice.getReplyList().forEach( x -> {
					this.replyList.add(new ReplyDto(x));
				});
			}
		}else {
			this.noticeId = 0;
		}
	}
	
	public Notice toEntity () {
		return Notice.builder()
				._id(this.noticeId)
				.userId(userId)
				.noticeTitle(this.noticeTitle)
				.noticeContent(this.noticeContent)
				.noticeImageFile(this.noticeImageFile)
				.noticeCount(this.noticeCount)
				.createDate(this.createDate)
				.updateDate(this.updateDate)
				.replyList(new ArrayList<Reply>())
				.build();
	}
}
