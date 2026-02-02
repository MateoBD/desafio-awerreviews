package com.awerreviews.desafio.interfaces.dao;

import com.awerreviews.desafio.interfaces.contracts.PageRequest;
import com.awerreviews.desafio.model.Task;
import org.springframework.data.domain.Page;

public interface TaskDao {

    Page<Task> getTasks(PageRequest pageRequest);

    Task createTask(String description);

}
