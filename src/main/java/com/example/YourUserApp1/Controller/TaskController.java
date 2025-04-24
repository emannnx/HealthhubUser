package com.example.YourUserApp1.Controller;

import com.example.YourUserApp1.WebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Task")
public class TaskController {

    private final WebClientService webClientService;

    @Autowired
    public TaskController(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @PostMapping("/createTask")
    public String postTask(@RequestBody Task task) {
        return webClientService.postWithWebClient(task);
    }

    @DeleteMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable int id) {
        return webClientService.deleteWithWebClient(id);
    }

    @GetMapping("/getAllTasks")
    public List<Object> getAllTasks() {
        return webClientService.getWithWebClient();
    }

    // ✅ Added updateTask method
    @PutMapping("/updateTask/{id}")
    public String updateTask(@PathVariable int id, @RequestBody Task task) {
        return webClientService.updateTaskWithWebClient(id, task);
    }

    // Inner Task class (if not defined elsewhere)
    public static class Task {
        private int id;
        private String task;
        private String status;

        public Task() {}

        public Task(int id, String task, String status) {
            this.id = id;
            this.task = task;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
