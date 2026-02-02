package com.awerreviews.desafio.interfaces.service;

import com.awerreviews.desafio.interfaces.contracts.PageRequest;
import com.awerreviews.desafio.model.Task;
import org.springframework.data.domain.Page;

public interface TaskService {

    Page<Task> getTasks(PageRequest pageRequest);

    Task createTask(String description);

}
