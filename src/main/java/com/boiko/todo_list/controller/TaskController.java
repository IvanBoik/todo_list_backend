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

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
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
        mongoClient = MongoClients
                .create("mongodb://" + user.getUser() + ":" + user.getPassword() + "@localhost:27017/ToDoList");
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

    @GetMapping("/group_by_done")
    public ResponseEntity<Map<Boolean, Integer>> groupByDone() {
        return ResponseEntity.ok(taskService.groupByDone());
    }

    @PostMapping("/filter_by_done")
    public ResponseEntity<List<Task>> filterByDone(@RequestBody boolean done) {
        return ResponseEntity.ok(taskService.filterByDone(done));
    }

    @PostMapping("/filter_by_date")
    public ResponseEntity<List<Task>> filterByDate(@RequestBody Date date) {
        return ResponseEntity.ok(taskService.filterByDate(date));
    }

    @GetMapping("/on_time")
    public ResponseEntity<List<Task>> completedOnTime() {
        return ResponseEntity.ok(taskService.completedOnTime());
    }

    @GetMapping("/not_on_time")
    public ResponseEntity<List<Task>> completedNotOnTime() {
        return ResponseEntity.ok(taskService.completedNotOnTime());
    }
}
