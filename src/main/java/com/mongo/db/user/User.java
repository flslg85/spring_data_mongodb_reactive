package com.mongo.db.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
@Accessors(chain = true)
@Setter
@Getter
@ToString
public class User {
    @Id
    private String id;

    @NotNull
    private String name;

    private int age;
}
