package com.mrcllw.tasklist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private boolean status;
    @ManyToOne
    private TaskList taskList;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    @JsonIgnore
    public TaskList getTaskList() {
        return taskList;
    }
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
