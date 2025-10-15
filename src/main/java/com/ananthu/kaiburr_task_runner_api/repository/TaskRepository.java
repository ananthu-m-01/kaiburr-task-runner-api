package com.ananthu.kaiburr_task_runner_api.repository;

import com.ananthu.kaiburr_task_runner_api.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByNameContainingIgnoreCase(String name);
}
