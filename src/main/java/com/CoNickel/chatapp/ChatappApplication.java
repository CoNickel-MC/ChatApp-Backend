package com.CoNickel.chatapp;

import com.CoNickel.chatapp.model.Messages;
import com.CoNickel.chatapp.model.User;
import com.CoNickel.chatapp.services.MessageService;
import com.CoNickel.chatapp.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
public class ChatappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatappApplication.class, args);
	}
}

@RestController
@RequestMapping("/root")
class Controller {
	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	private final List<SseEmitter> emitters = new ArrayList<>();

	@GetMapping(value = "/stream/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter stream(@PathVariable String receiver) {
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitters.add(emitter);

		emitter.onCompletion(() -> emitters.remove(emitter));
		emitter.onTimeout(() -> emitters.remove(emitter));

		return emitter;
	}

	private void notifyClients(Messages newMessage) {
		List<SseEmitter> deadEmitters = new ArrayList<>();

		emitters.forEach(emitter -> {
			try {
				emitter.send(SseEmitter.event().data(newMessage));
			} catch (Exception e) {
				deadEmitters.add(emitter);
			}
		});

		emitters.removeAll(deadEmitters);
	}


	@GetMapping
	public ResponseEntity<List<User>> showAll() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
	}


	@PostMapping("/login")
	@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
	boolean checkLoginCreds(@RequestBody User user) {
		Optional<User> temp = userService.getUserByUsername(user.getUsername());
		if(temp.isPresent()) {
			if (Objects.equals(temp.get().getPassword(), user.getPassword()))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	@PostMapping("/addUser")
	@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
	boolean RegisterUser(@RequestBody User user) {
		Optional<User> temp = userService.getUserByUsername(user.getUsername());
		if(temp.isPresent())
			return false;
		
		else {
			userService.addUser(user);

			return true;
		}

	}

	@PostMapping("/getChats")
	@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8099"})
	List<Messages> getChats(@RequestBody customMessage user) {
		return messageService.getChatsForUser(user.getSender(), user.getReceiver());
	}

	@PostMapping("/getSenders")
	@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8099"})
	List<String> getSenders(@RequestBody customMessage user) {
		List<Messages> temp = messageService.getChatsForSingleUser(user.getSender());
		List<String> receivers = new ArrayList<>();
		for (Messages a: temp) {
			if (!receivers.contains(a.getReceiver()) ){
				receivers.add(a.getReceiver());
			}
			if (!receivers.contains(a.getSender()) ){
				receivers.add(a.getSender());
			}
		}
		return receivers;
	}

	@PostMapping("/sendChat")
	@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8099"})
	void sendChat(@RequestBody Messages newMessage) {
		messageService.saveMessage(newMessage);
		notifyClients(newMessage);
	}
}


@Getter
class customMessage{
	private String sender;

	private String receiver;
}
