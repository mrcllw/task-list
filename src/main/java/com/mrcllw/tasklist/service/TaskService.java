package com.mrcllw.tasklist.service;

import com.mrcllw.tasklist.model.Task;
import com.mrcllw.tasklist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task save (Task task){
        return taskRepository.save(task);
    }

    public Task edit (Task task){
        return taskRepository.save(task);
    }

    public Task findById(Long id){
        return taskRepository.findTaskById(id);
    }

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public void remove(Long id){
        taskRepository.deleteById(id);
    }
}
