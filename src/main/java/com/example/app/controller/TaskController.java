package com.example.app.controller;

import com.example.app.entity.Task;
import com.example.app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";  // matches tasks.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "addTask";  // matches addTask.html
    }

    @PostMapping("/add")
    public String saveTask(@ModelAttribute Task task) {
        task.setId(TaskService.nextId());
        task.setCreatedAt(LocalDateTime.now());
        taskService.addTask(task);
        return "redirect:/tasks";
    }
}
