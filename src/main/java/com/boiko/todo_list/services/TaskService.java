package com.boiko.todo_list.services;

import com.boiko.todo_list.entity.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.internal.MongoClientImpl;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private MongoCollection<Document> collection =
            MongoClients.create("mongodb://UserPlanner:UserPlanner@localhost:27017/ToDoList")
                    .getDatabase("ToDoList")
                    .getCollection("tasks");


    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;

    }

    public void insert(Task task) {
        Document document = taskToDocument(task);
        collection.insertOne(document);
    }

    public void delete(Task task) {
        Document document = new Document();
        document.put("_id", new ObjectId(task.getId()));
        collection.deleteOne(document);
    }

    public void update(Task task) {
        Document update = new Document();
        Document object = taskToDocument(task);
        update.put("$set", object);

        BasicDBObject query = new BasicDBObject();
        query.append("_id", new ObjectId(task.getId()));
        collection.updateOne(query, update);
    }

    public List<Task> selectAll() {
        FindIterable<Document> findIterable = collection.find();
        List<Task> tasks = new ArrayList<>();
        findIterable.forEach(x -> tasks.add(documentToTask(x)));
        return tasks;
    }

    public Document selectByID(String id) {
        Document query = new Document();
        query.put("_id", id);
        return collection.find(query).first();
    }

    private Document taskToDocument(Task task) {
        Document document = new Document();
        if (task.getId() != null) {
            document.put("_id", new ObjectId(task.getId()));
        }

        document.put("name", task.getName());
        document.put("description", task.getDescription());
        document.put("done", task.isDone());
        document.put("date", task.getDate());
        return document;
    }

    private Task documentToTask(Document document) {
        Date date = document.getDate("date");

        Object getDone = document.get("isDone");
        boolean done = getDone != null && (boolean) getDone;
        return Task.builder()
                .id(document.get("_id").toString())
                .name(document.get("name").toString())
                .description(document.get("description").toString())
                .date(date)
                .isDone(done)
                .build();
    }
}
