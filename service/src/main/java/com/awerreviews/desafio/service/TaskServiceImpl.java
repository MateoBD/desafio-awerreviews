package com.awerreviews.desafio.service;

import com.awerreviews.desafio.interfaces.dao.TaskDao;
import com.awerreviews.desafio.interfaces.service.TaskService;
import com.awerreviews.desafio.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasks(PageRequest pageRequest) {
        return taskDao.getTasks(pageRequest);
    }

    @Override
    @Transactional
    public Task createTask(String description) {
        return taskDao.createTask(description);
    }
}
