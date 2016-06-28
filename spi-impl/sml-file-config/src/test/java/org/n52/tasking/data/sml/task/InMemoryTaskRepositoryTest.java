package org.n52.tasking.data.sml.task;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class InMemoryTaskRepositoryTest {

    private TaskRepository repository;

    @Before
    public void setUp() {
        repository = new InMemoryTaskRepository();
    }

    @Test
    public void when_emptyRepository_then_emptyList() {
        assertThat(repository.getTasks(), is(empty()));
    }
    
    @Test
    public void when_addingTask_then_expectNonNullTask() {
        CreateTask cmd = new CreateTask();
        cmd.setId("42");
        cmd.setParameters("some-fancy-parameters-here");
        assertThat(repository.createTask(cmd), notNullValue(Task.class));
    }
    
    @Test
    public void when_addingTask_then_expectValidTask() {
        final String id = "42";
        final String parameters = "some-fancy-parameters-here";
        
        CreateTask cmd = new CreateTask();
        cmd.setId(id);
        cmd.setParameters(parameters);
        Task task = repository.createTask(cmd);
        assertThat(task.getId(), is(id));
        assertThat(task.getEncodedParameters(), is(parameters));
    }
    
    @Test
    public void when_addingInvalidTask_then_expectRejectedTask() {
        final String id = "42";
        final String parameters = "some-fancy-parameters-here";
    }
}
