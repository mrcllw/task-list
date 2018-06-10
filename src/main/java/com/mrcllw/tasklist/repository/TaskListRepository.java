package com.mrcllw.tasklist.repository;

import com.mrcllw.tasklist.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByUserId(Long id);
    TaskList findTaskListById(Long id);
}
