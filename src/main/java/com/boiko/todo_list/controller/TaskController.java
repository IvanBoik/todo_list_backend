package com.boiko.todo_list.controller;

import com.boiko.todo_list.entity.AuthRequest;
import com.boiko.todo_list.entity.Task;
import com.boiko.todo_list.services.TaskService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private MongoClient mongoClient;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getTaskByID(@PathVariable String id) {
        return ResponseEntity.ok(taskService.selectByID(id));
    }

    @PostMapping("/")
    public void createTask(@RequestBody Task task) {
        taskService.insert(task);
    }

    @PostMapping("/connect")
    public void mongoConnection(@RequestBody AuthRequest user) {
        if (this.mongoClient != null) {
            mongoClient.close();
        }
        mongoClient = MongoClients.create("mongodb://" + user + ":" + user + "@localhost:27017/ToDoList");
        taskService.setCollection(mongoClient.getDatabase("ToDoList").getCollection("tasks"));
    }

    @PutMapping("/")
    public void updateTask(@RequestBody Task task) {
        taskService.update(task);
    }

    @DeleteMapping("/")
    public void deleteTask(@RequestBody Task task) {
        taskService.delete(task);
    }
}
