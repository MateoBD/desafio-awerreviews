package com.awerreviews.desafio.persistence;

import com.awerreviews.desafio.interfaces.dao.TaskDao;
import com.awerreviews.desafio.model.Task;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TaskJpaDao implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Task> getTasks(PageRequest pageRequest) {
        Query nativeQuery = entityManager.createNativeQuery(
                "SELECT id " +
                        "FROM tasks "
        );

        nativeQuery.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        nativeQuery.setMaxResults(pageRequest.getPageSize());

        @SuppressWarnings("unchecked")
        List<Number> rawIds = (List<Number>) nativeQuery.getResultList();

        // Obtenemos el total de elementos SIEMPRE
        long totalElements = ((Number) entityManager.createNativeQuery(
                "SELECT COUNT(id) FROM tasks"
        ).getSingleResult()).longValue();

        if (rawIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageRequest, totalElements);
        }

        List<Long> filteredIds = rawIds.stream()
                .map(Number::longValue)
                .collect(Collectors.toList());

        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t " +
                        "FROM Task t " +
                        "WHERE t.id IN :ids ",
                Task.class
        );

        query.setParameter("ids", filteredIds);

        return new PageImpl<>(
                query.getResultList(),
                pageRequest,
                totalElements
        );
    }

    @Override
    public Task createTask(String description) {
        Task task = new Task(description);
        entityManager.persist(task);
        return task;
    }

}
