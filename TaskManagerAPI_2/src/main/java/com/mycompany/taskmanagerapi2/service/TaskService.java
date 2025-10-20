package com.mycompany.taskmanagerapi2.service;

import com.mycompany.taskmanagerapi2.model.Task;
import com.mycompany.taskmanagerapi2.model.TaskExecution;
import com.mycompany.taskmanagerapi2.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        // Add validation as needed
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public List<Task> searchTasksByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    // Execute shell command & save execution details
    public Task executeTaskCommand(Task task) {
        try {
            Instant startTime = Instant.now();

            // For Windows, execute with cmd /c
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", task.getCommand()});

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder outputBuilder = new StringBuilder();

            String line;
            while ((line = stdOut.readLine()) != null) {
                outputBuilder.append(line).append(System.lineSeparator());
            }
            while ((line = stdErr.readLine()) != null) {
                outputBuilder.append(line).append(System.lineSeparator());
            }

            int exitCode = process.waitFor();

            Instant endTime = Instant.now();

            TaskExecution exec = new TaskExecution(startTime, endTime, outputBuilder.toString());

            List<TaskExecution> executions = task.getTaskExecutions();
            if (executions == null) {
                executions = new ArrayList<>();
            }
            executions.add(exec);
            task.setTaskExecutions(executions);

            return saveTask(task);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute command: " + e.getMessage(), e);
        }
    }
}
