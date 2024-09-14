package com.CoNickel.chatapp.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
	@Id
	private ObjectId objectId;

	@Getter
	private String sender;

	@Getter
	private String receiver;

	private String content;

	@Getter
	@Setter
	private long time;
}
