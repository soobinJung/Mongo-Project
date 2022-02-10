package com.noti.api.notice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.noti.document.Notice;

public interface NoticeRepository extends MongoRepository<Notice, Long>{
}
