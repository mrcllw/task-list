package com.mrcllw.tasklist.service;

import com.mrcllw.tasklist.model.TaskList;
import com.mrcllw.tasklist.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    public TaskList save (TaskList taskList){
        return taskListRepository.save(taskList);
    }

    public TaskList edit (TaskList taskList){
        return taskListRepository.save(taskList);
    }

    public TaskList findById(Long id){
        return taskListRepository.findTaskListById(id);
    }

    public List<TaskList> getLists(){
        return taskListRepository.findAll();
    }

    public void remove(Long id){
        taskListRepository.deleteById(id);
    }

    public List<TaskList> findByUserId(Long id){
        return taskListRepository.findByUserId(id);
    }
}
