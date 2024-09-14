package com.CoNickel.chatapp.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface MessagesRepository extends MongoRepository<Messages, ObjectId> {
	List<Messages> findAllBySenderAndReceiver(String sender, String receiver);
	List<Messages> findAllBySender(String sender);
	List<Messages> findAllByReceiver(String receiver);


}
