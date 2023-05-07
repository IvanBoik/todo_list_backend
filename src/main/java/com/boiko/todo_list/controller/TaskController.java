package com.boiko.todo_list.controller;

import com.boiko.todo_list.entity.Task;
import com.boiko.todo_list.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskByID(@PathVariable String id) {
        return taskRepository.existsById(id)
                ? ResponseEntity.ok(taskRepository.findById(id).orElseThrow())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return taskRepository.existsById(task.getId())
                ? ResponseEntity.ok(taskRepository.save(task))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/")
    public boolean deleteTask(@RequestBody Task task) {
        try {
            taskRepository.delete(task);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteTaskByID(@PathVariable String id) {
        try {
            taskRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
