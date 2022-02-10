package com.noti.api.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.noti.document.User;

public interface UserRepository extends MongoRepository<User, Long> {

}
