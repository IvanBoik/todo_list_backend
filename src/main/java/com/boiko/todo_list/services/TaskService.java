package com.boiko.todo_list.services;

import com.boiko.todo_list.entity.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskService {

    @Autowired
    private MongoCollection<Document> collection;

    private static final Date nullDate = new Date(0);


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
        document.put("deadline", task.getDeadline());
        document.put("dateOfComplete", nullDate.equals(task.getDateOfComplete()) ? null : task.getDateOfComplete());
        return document;
    }

    private Task documentToTask(Document document) {
        Object getDone = document.get("done");
        boolean done = getDone != null && (boolean) getDone;
        return Task.builder()
                .id(document.get("_id").toString())
                .name(document.get("name").toString())
                .description(document.get("description").toString())
                .deadline(document.getDate("deadline"))
                .DateOfComplete(nullDate.equals(document.getDate("dateOfComplete")) ? null : document.getDate("dateOfComplete"))
                .done(done)
                .build();
    }

    public List<Task> filterByDone(boolean value) {
        List<Task> result = new ArrayList<>();
        collection.aggregate(
                List.of(
                        Aggregates.match(Filters.eq("done", value))
                )
        ).forEach(this::documentToTask);
        return result;
    }

    public List<Task> filterByDate(Date date) {
        List<Task> result = new ArrayList<>();
        collection.aggregate(
                List.of(
                        Aggregates.match(Filters.eq("deadline", date))
                )
        ).forEach(this::documentToTask);
        return result;
    }

    public Map<Boolean, Integer> groupByDone() {
        Map<Boolean, Integer> res = new HashMap<>();
        AtomicInteger trueCount = new AtomicInteger();
        AtomicInteger falseCount = new AtomicInteger();
        collection.aggregate(
                List.of(
                        Aggregates.match(Filters.eq("done", true))
                )
        ).forEach((x) -> trueCount.getAndIncrement());
        collection.aggregate(
                List.of(
                        Aggregates.match(Filters.eq("done", false))
                )
        ).forEach((x) -> falseCount.getAndIncrement());

        res.put(true, trueCount.get());
        res.put(false, falseCount.get());

        return res;
    }

    public List<Task> completedOnTime() {
        List<Task> result = new ArrayList<>();

        collection.find().forEach(x ->
        {
            if(x.getDate("DateOfComplete").before(x.getDate("deadline")))
                result.add(documentToTask(x));
        });

        return result;
    }

    public List<Task> completedNotOnTime() {
        List<Task> result = new ArrayList<>();

        collection.find().forEach(x ->
        {
            if(x.getDate("DateOfComplete").after(x.getDate("deadline")))
                result.add(documentToTask(x));
        });

        return result;
    }
}
