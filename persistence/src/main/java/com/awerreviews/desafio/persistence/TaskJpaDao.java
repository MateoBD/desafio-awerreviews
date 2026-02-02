package com.awerreviews.desafio.persistence;

import com.awerreviews.desafio.interfaces.dao.TaskDao;
import com.awerreviews.desafio.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public class TaskJpaDao implements TaskDao {

    @Override
    public Page<Task> getTasks(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Task createTask(String description) {
        return null;
    }
}
