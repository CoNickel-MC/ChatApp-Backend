package com.CoNickel.chatapp.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
	Optional<User> findUserById(short id);
	Optional<User> findUserByUsername(String username);
}
