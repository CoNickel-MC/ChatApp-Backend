package com.CoNickel.chatapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	private ObjectId ObjectId;

	@Getter
	private String username;

	private short id;

	@Getter
	private String password;

}
