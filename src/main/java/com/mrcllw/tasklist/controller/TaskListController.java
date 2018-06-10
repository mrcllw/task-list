package com.mrcllw.tasklist.controller;

import com.mrcllw.tasklist.model.Task;
import com.mrcllw.tasklist.model.TaskList;
import com.mrcllw.tasklist.model.User;
import com.mrcllw.tasklist.service.TaskListService;
import com.mrcllw.tasklist.service.TaskService;
import com.mrcllw.tasklist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-list")
public class TaskListController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public ResponseEntity getLists(@RequestParam Long userId){
        User userParam = userService.findById(userId);
        if(userParam == null){
            return ResponseEntity.status(404).body("User not exists.");
        }
        List<TaskList> taskLists = taskListService.findByUserId(userId);
        return ResponseEntity.status(200).body(taskLists);
    }

    @PostMapping("")
    public ResponseEntity addList(@RequestBody TaskList taskList){
        User user = userService.findById(taskList.getUser().getId());
        if(user == null){
            return ResponseEntity.status(404).body("User not exists.");
        }
        taskList.setUser(user);
        taskListService.save(taskList);
        return ResponseEntity.status(201).body(taskList);
    }

    @PutMapping("/{idList}")
    public ResponseEntity editList(@PathVariable Long idList,
                                   @RequestBody TaskList taskList){
        TaskList taskListAtt = taskListService.findById(idList);
        if(taskListAtt == null){
            return ResponseEntity.status(404).body("List not exists.");
        }
        taskListAtt.setName(taskList.getName());
        return ResponseEntity.status(200).body(taskListService.edit(taskListAtt));
    }

    @DeleteMapping("/{idList}")
    public ResponseEntity removeList(@PathVariable Long idList){
        TaskList taskListRemove = taskListService.findById(idList);
        if(taskListRemove == null){
            return ResponseEntity.status(404).body("List not exists.");
        }
        taskListService.remove(idList);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/{idList}/tasks")
    public ResponseEntity addTask(@PathVariable Long idList,
                                  @RequestBody Task task){
        TaskList taskList = taskListService.findById(idList);
        if(taskList == null){
            return ResponseEntity.status(404).body("List not exists.");
        }
        task.setTaskList(taskList);
        return ResponseEntity.status(201).body(taskService.save(task));
    }

    @PutMapping("/{idList}/tasks/{idTask}")
    public ResponseEntity editStatus(@PathVariable Long idList,
                                     @PathVariable Long idTask,
                                     @RequestBody String status){
        TaskList taskList = taskListService.findById(idList);
        if(taskList == null){
            return ResponseEntity.status(404).body("List not exists.");
        }
        Task task = taskService.findById(idTask);
        if(task == null){
            return ResponseEntity.status(404).body("Task not exists.");
        }
        task.setStatus(Boolean.valueOf(status));
        return ResponseEntity.status(200).body(taskService.save(task));
    }
}
