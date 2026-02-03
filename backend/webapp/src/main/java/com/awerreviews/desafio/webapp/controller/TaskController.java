package com.awerreviews.desafio.webapp.controller;

import com.awerreviews.desafio.interfaces.service.TaskService;
import com.awerreviews.desafio.model.Task;
import com.awerreviews.desafio.webapp.form.PostTaskForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Component
@Path("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> qp = uriInfo.getQueryParameters();
        
        int pageNumber = Optional.ofNullable(qp.getFirst("pageNumber"))
                .map(Integer::parseInt).orElse(1);
        int pageSize = Optional.ofNullable(qp.getFirst("pageSize"))
                .map(Integer::parseInt).orElse(20);
        
        if (pageNumber < 1 || pageSize < 1 || pageSize > 100) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Page<Task> tasksPage = taskService.getTasks(PageRequest.of(pageNumber - 1, pageSize));

        Response.ResponseBuilder builder =
                Response.ok(new GenericEntity<List<Task>>(tasksPage.getContent()) {});

        StringBuilder link = new StringBuilder();

        link.append("<").append(uriInfo.getRequestUriBuilder()
                .replaceQueryParam("pageNumber", 1)
                .replaceQueryParam("pageSize", pageSize)
                .build()).append(">; rel=\"first\"");

        if (pageNumber > 1) {
            link.append(", <").append(uriInfo.getRequestUriBuilder()
                    .replaceQueryParam("pageNumber", pageNumber - 1)
                    .replaceQueryParam("pageSize", pageSize)
                    .build()).append(">; rel=\"prev\"");
        }

        if (tasksPage.hasNext()) {
            link.append(", <").append(uriInfo.getRequestUriBuilder()
                    .replaceQueryParam("pageNumber", pageNumber + 1)
                    .replaceQueryParam("pageSize", pageSize)
                    .build()).append(">; rel=\"next\"");
        }

        // getTotalPages() devuelve el número correcto de páginas
        // Como usamos indexación base-1 para el usuario, este valor ya es correcto
        int lastPageNumber = Math.max(1, tasksPage.getTotalPages());
        link.append(", <").append(uriInfo.getRequestUriBuilder()
                .replaceQueryParam("pageNumber", lastPageNumber)
                .replaceQueryParam("pageSize", pageSize)
                .build()).append(">; rel=\"last\"");

        builder.header("Link", link.toString());

        return builder.build();
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON, })
    @Produces(value = {MediaType.APPLICATION_JSON, })
    public Response createTask(@Valid PostTaskForm postTaskForm) {
        if (postTaskForm == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Request body is required\"}")
                    .build();
        }
        Task createdTask = taskService.createTask(postTaskForm.getDescription());
        return Response.status(Response.Status.CREATED).entity(createdTask).build();
    }

}
