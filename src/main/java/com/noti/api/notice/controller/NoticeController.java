package com.noti.api.notice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.noti.api.notice.dto.NoticeDto;
import com.noti.api.notice.dto.NoticeSearch;
import com.noti.api.notice.dto.ReplyDto;
import com.noti.api.notice.repository.NoticeRepository;
import com.noti.api.notice.service.NoticeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("notice")
@RestController
public class NoticeController {

	NoticeService service;
	NoticeRepository repository;
	
	@GetMapping("")
	@ResponseBody
	public ResponseEntity<Object> getNoticeByAll(NoticeSearch noticeSearch) {
		return ResponseEntity.ok().body(
			service.getNoticeByAll(noticeSearch)
		);
	}

	@GetMapping("/{noticeId}")
	@ResponseBody
	public ResponseEntity<NoticeDto> getNoticeByNoticeId( @PathVariable(name = "noticeId", required = true) long noticeId ) {
		return ResponseEntity.ok().body(
			service.getNoticeById(noticeId)
		);
	}
	
	@PostMapping("")
	@ResponseBody
	public ResponseEntity<NoticeDto> insertNotice( NoticeDto noticeDto ) {
		return ResponseEntity.ok().body(
			service.insertNotice(noticeDto)
		);
	}
	
	@PatchMapping("/{noticeId}")
	@ResponseBody
	public ResponseEntity<UpdateResult> updateNoticeById( @PathVariable(name = "noticeId", required = true) long noticeId, NoticeDto noticeDto ) {
		return ResponseEntity.ok().body(
			service.updateNoticeById(noticeId, noticeDto)
		);
	}
	
	@DeleteMapping("/{noticeId}")
	@ResponseBody
	public ResponseEntity<DeleteResult> deleteNoticeByNoticeId( @PathVariable(name = "noticeId", required = true) long noticeId ) {
		return ResponseEntity.ok().body(
			service.deleteNoticeByNoticeId(noticeId)
		);
	}
	
	@PostMapping("/{noticeId}/reply")
	@ResponseBody
	public ResponseEntity<NoticeDto> insertReplyByNoticeId( @PathVariable(name = "noticeId", required = true) long noticeId, ReplyDto replyDto) {
		
		return ResponseEntity.ok().body(
			service.insertReplyByNoticeId(noticeId, replyDto)
		);
	}
	
	@DeleteMapping("/{noticeId}/reply/{replyId}")
	@ResponseBody
	public ResponseEntity<NoticeDto> deleteReplyByReplyId( @PathVariable(name = "noticeId", required = true) long noticeId, @PathVariable(name = "replyId", required = true) long replyId) {
		
		return ResponseEntity.ok().body(
			service.deleteReplyByReplyId(noticeId, replyId)
		);
	}
}