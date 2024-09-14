package com.CoNickel.chatapp.services;

import com.CoNickel.chatapp.model.Messages;
import com.CoNickel.chatapp.model.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;


@Service
public class MessageService {
	@Autowired
	private MessagesRepository messagesRepository;

	public List<Messages> getChatsForUser(String username, String chatOpened) {
		List<Messages> temp = messagesRepository.findAllBySenderAndReceiver(username, chatOpened);
		temp.addAll(messagesRepository.findAllBySenderAndReceiver(chatOpened, username));
		temp.sort(Comparator.comparingLong(Messages::getTime));

		return temp;
	}
	public List<Messages> getChatsForSingleUser(String username) {
		List<Messages> temp = messagesRepository.findAllBySender(username);
		temp.addAll(messagesRepository.findAllByReceiver(username));
		return temp;
	}

	public void saveMessage(Messages newMessage) {
		newMessage.setTime(Instant.now().toEpochMilli());
		messagesRepository.save(newMessage);
	}



}
