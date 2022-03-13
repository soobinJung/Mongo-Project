package com.noti.api.notice.service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.noti.api.notice.dto.NoticeDto;
import com.noti.api.notice.dto.NoticeSearch;
import com.noti.api.notice.dto.ReplyDto;
import com.noti.api.user.dto.UserDto;
import com.noti.api.user.service.UserService;
import com.noti.document.Notice;
import com.noti.util.DateProcess;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@AllArgsConstructor
@Service
public class NoticeService {

	MongoTemplate template;
	UserService userService;
	DateProcess date;
	
	static final String DATEFORMAT = "yyyy/MM/dd";
	
	/**
	 * 게시글 전체를 반환
	 */
	public List<NoticeDto> getNoticeByAll(NoticeSearch noticeSearch) {
		
		Query qurey = new Query();
		 
		// 제목, 내용  검색
		if(noticeSearch.isSearch()) {
			qurey.addCriteria(
				Criteria.where(noticeSearch.getSearchOptionName())
				 		.regex(".*" +noticeSearch.getSearchOptionValue() + ".*") 
			);
		}
		
		// 정렬
		if(noticeSearch.isOrder()) {
			qurey.with(Sort.by(
				noticeSearch.getOrderOptionValue().equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC
				, noticeSearch.getOrderOptionName()
			));
		}
		
		// 페이징
		if( noticeSearch.getSize() > 0 ) {
			qurey.with( PageRequest.of(0, noticeSearch.getSize()));
			qurey.skip( (noticeSearch.getPage() - 1 ) * 10 );
		}
		
		List<NoticeDto> noticeList = template.find(qurey, Notice.class).stream().map(vo -> new NoticeDto(vo)).collect(Collectors.toList());
		
		// 사용자 정보 세팅
		noticeList.forEach( x -> {
			x.setUser(userService.getUserInfo(x.getUserId()));
		});
		
		return noticeList;
	}

	/**
	 * 게시글 단일조회
	 */
	public NoticeDto getNoticeById(long noticeId) {
		Criteria criteria = new Criteria("_id");
		criteria.is(noticeId);
		
		Query query = new Query(criteria);
		NoticeDto noticeDto = new NoticeDto(template.findOne(query, Notice.class));
		
		// 조회수 증가
		Update update = new Update();
		update.set("noticeCount", noticeDto.getNoticeCount() + 1 );
		template.updateFirst( new Query(criteria), update, Notice.class );
		
		// 사용자 정보 세팅
		NoticeDto notice = new NoticeDto(template.findOne(query, Notice.class));
		notice.setUser(userService.getUserInfo(notice.getUserId()));
		
		return notice;
	}

	/**
	 * noticeId 의 게시글 등록
	 * @throws AuthenticationException 
	 */
	public NoticeDto insertNotice( NoticeDto noticeDto ) throws AuthenticationException {

		UserDto user = userService.getUserInfo();
		
		if(!user.isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		// 몽고는 auto increment 기능 없음 id 생성 
		long noticeId = template.count(new Query(), Notice.class) + 1;
		
		// 유니크 키 생성
		while (getNoticeById(noticeId).getNoticeId() > 0) {
			noticeId ++;
		} 
		
		noticeDto.setUserId(user.getUserId());
		noticeDto.setNoticeId(noticeId);
		noticeDto.setNoticeCount(0);
		noticeDto.setCreateDate(date.getTodayDate(DATEFORMAT));
		noticeDto.setUpdateDate(date.getTodayDate(DATEFORMAT));
		
		// 게시물 등록
		noticeDto = new NoticeDto(template.insert(noticeDto.toEntity()));
		
		// 사용자 정보 세팅
		noticeDto.setUser(userService.getUserInfo(noticeDto.getUserId()));
		
		return noticeDto;
	}

	/**
	 * noticeId 의 게시글 수정
	 * @throws AuthenticationException 
	 */
	public UpdateResult updateNoticeById(long noticeId, NoticeDto updateDto) throws AuthenticationException  {
		
		if(!userService.getUserInfo().isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		Criteria criteria = new Criteria("_id");
		criteria.is(noticeId);
		
		Update update = new Update();
		update.set("noticeTitle", updateDto.getNoticeTitle());
		update.set("noticeContent", updateDto.getNoticeContent());
		update.set("noticeImageFile", updateDto.getNoticeImageFile());
		update.set("updateDate", date.getTodayDate(DATEFORMAT));
		
		return template.updateFirst( new Query(criteria), update, Notice.class );
	}

	/**
	 * noticeId 의 게시글 삭제
	 * @throws AuthenticationException 
	 */
	public DeleteResult deleteNoticeByNoticeId(long noticeId) throws AuthenticationException {

		if(!userService.getUserInfo().isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		Query query = new Query(new Criteria("_id").is(noticeId));
		return template.remove(query,Notice.class);
	}

	/**
	 * 게시글의 댓글 등록
	 * @throws AuthenticationException 
	 */
	public NoticeDto insertReplyByNoticeId(long noticeId, ReplyDto replyDto) throws AuthenticationException {

		if(!userService.getUserInfo().isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		NoticeDto noticeDto = getNoticeById(noticeId);
		
		// 몽고는 auto increment 기능 없음 id 생성 
		long replyId = noticeDto.getReplyList().size() + 1;
		
		// 유니크 키 생성
		while (getNoticeById(noticeId).getNoticeId() > 0) {
			
			long localReplyId = replyId;
			long overlap = noticeDto.getReplyList().stream().filter( x -> {
				return localReplyId == x.getReplyId();
			}).count();
			
			if(overlap > 0) {
				replyId++;
			}else {
				break;
			}
		} 
		
		replyDto.setReplyId(replyId);
		replyDto.setUserId(userService.getUserInfo().getUserId());
		replyDto.setCreateDate(date.getTodayDate(DATEFORMAT));
		replyDto.setUpdateDate(date.getTodayDate(DATEFORMAT));
		noticeDto.getReplyList().add(replyDto);
		
		Criteria criteria = new Criteria("_id");
		criteria.is(noticeId);
		
		Update update = new Update();
		update.set("replyList", noticeDto.getReplyList().stream().map(x -> x.toEntity()).collect(Collectors.toList()));
		template.updateFirst( new Query(criteria), update, Notice.class );
		
		// 사용자 정보 세팅
		noticeDto.setUser(userService.getUserInfo(noticeDto.getUserId()));
		
		return noticeDto;
	}

	/**
	 * 게시글의 댓글 수정
	 * @throws Exception 
	 */
	public NoticeDto updateReplyByReplyId(long noticeId, long replyId, ReplyDto updateDto ) throws AuthenticationException {

		UserDto user = userService.getUserInfo();
		
		if(!user.isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		NoticeDto noticeDto = getNoticeById(noticeId); 
		noticeDto.getReplyList().forEach( reply -> {
			if(reply.getReplyId() == replyId && reply.getUserId() == user.getUserId()) {
				reply.setReplycontent(updateDto.getReplycontent());
				reply.setUpdateDate(date.getTodayDate(DATEFORMAT));
			}
		});
		
		Criteria criteria = new Criteria("_id");
		criteria.is(noticeId);
		
		Update update = new Update();
		update.set("replyList", noticeDto.getReplyList().stream().map(x -> x.toEntity()).collect(Collectors.toList()));
		template.updateFirst( new Query(criteria), update, Notice.class );
		
		// 사용자 정보 세팅
		noticeDto.setUser(userService.getUserInfo(noticeDto.getUserId()));
				
		return noticeDto;
	}
	
	/**
	 * 게시글의 댓글 삭제
	 * @throws Exception 
	 */
	public NoticeDto deleteReplyByReplyId(long noticeId, long replyId) throws AuthenticationException {

		if(!userService.getUserInfo().isLoginState()) {
			throw new AuthenticationException("No Login");
		}
		
		NoticeDto noticeDto = getNoticeById(noticeId); 
		Iterator<ReplyDto> it = noticeDto.getReplyList().iterator();
		while (it.hasNext()) {
			if (it.next().getReplyId() == replyId) {
				it.remove();
			}
		}
			
		Criteria criteria = new Criteria("_id");
		criteria.is(noticeId);
		
		Update update = new Update();
		update.set("replyList", noticeDto.getReplyList().stream().map(x -> x.toEntity()).collect(Collectors.toList()));
		template.updateFirst( new Query(criteria), update, Notice.class );
		
		// 사용자 정보 세팅
		noticeDto.setUser(userService.getUserInfo(noticeDto.getUserId()));
				
		return noticeDto;
	}
}
