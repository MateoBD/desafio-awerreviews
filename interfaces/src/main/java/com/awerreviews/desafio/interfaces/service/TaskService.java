package com.awerreviews.desafio.interfaces.service;


import com.awerreviews.desafio.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TaskService {

    Page<Task> getTasks(PageRequest pageRequest);

    Task createTask(String description);

}
