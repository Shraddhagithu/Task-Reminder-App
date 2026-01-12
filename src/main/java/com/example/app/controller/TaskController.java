package com.example.app.controller;

import com.example.app.entity.Task;
import com.example.app.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // LIST TASKS
    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    // SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "add-task";
    }

    // SAVE TASK
    @PostMapping("/add")
    public String saveTask(@ModelAttribute Task task) {
        taskService.addTask(task);
        return "redirect:/tasks";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        model.addAttribute("task", task);
        return "edit-task";
    }

    // UPDATE TASK
    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task task, Model model) {

        Task existing = taskService.findById(id)
                .orElse(null);

        if (existing == null) {
            model.addAttribute("errorMessage", "Task not found");
            return "edit-task";
        }

        // Validate title
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Title is required!");
            return "edit-task";
        }

        // Preserve ID and created date
        task.setId(id);
        task.setCreatedAt(existing.getCreatedAt());

        // Update the task
        taskService.updateTask(task);

        return "redirect:/tasks";
    }

    // DELETE TASK
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Model model) {
        if (!taskService.findById(id).isPresent()) {
            model.addAttribute("errorMessage", "Task not found");
            return "tasks";
        }
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    // CHECK TASK (mark as done)
    @GetMapping("/check/{id}")
    public String checkTask(@PathVariable Long id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus("Done");
        taskService.updateTask(task);
        return "redirect:/tasks";
    }
}
