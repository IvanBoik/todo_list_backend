package com.boiko.todo_list.repo;

import com.boiko.todo_list.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
