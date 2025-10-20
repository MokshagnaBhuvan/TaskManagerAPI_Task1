package com.mycompany.taskmanagerapi2.controller;

import com.mycompany.taskmanagerapi2.model.Task;
import com.mycompany.taskmanagerapi2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.searchTasksByName(""); // Return all tasks
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        return taskOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public Task createOrUpdateTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Task> searchTasksByName(@RequestParam String name) {
        return taskService.searchTasksByName(name);
    }

    @PutMapping("/{id}/execute")
    public ResponseEntity<Task> executeTaskCommand(@PathVariable("id") String id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task updatedTask = taskService.executeTaskCommand(taskOpt.get());
        return ResponseEntity.ok(updatedTask);
    }
}
