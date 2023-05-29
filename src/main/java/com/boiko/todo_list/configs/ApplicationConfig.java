package com.boiko.todo_list.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public MongoClient defaultMongoClient() {
        return MongoClients.create("mongodb://UserPlanner:UserPlanner@localhost:27017/ToDoList");
    }

    @Bean
    public MongoCollection<Document> defaultCollection(MongoClient client) {
        return client.getDatabase("ToDoList").getCollection("tasks");
    }
}
