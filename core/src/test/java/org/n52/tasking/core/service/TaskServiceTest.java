/*
 * Copyright (C) 2016-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package org.n52.tasking.core.service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.n52.tasking.data.TaskRunner;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class TaskServiceTest {

    private TaskService taskService;

    private TaskRepository repository;

    private TaskRunner taskRunnerMock;

    @Before
    public void setUp() {
        taskRunnerMock = mockTaskRunner();
        taskService = new TaskService(taskRunnerMock);
        repository = mock(TaskRepository.class);
        taskService.setRepository(repository);
    }

    private TaskRunner mockTaskRunner() {
        TaskRunner mock = mock(TaskRunner.class);
        when(mock.getExecutorService()).thenReturn(Executors.newSingleThreadExecutor());
        return mock;
    }

    @Test
    @Ignore("FIX: mocked getRunnable() is not called during asyncExec()")
    public void when_addingTask_then_expectTaskToBeRun() {
        Task task = new Task();
        task.setId("someId");
        task.setSubmittedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        when(repository.createTask(any())).thenReturn(task);
        when(taskRunnerMock.getRunnable(task)).thenReturn((Runnable) () -> {
            // do nothing
        });

        final String deviceId = "42";
        Resource resource = taskService.createTask(new CreateTask(deviceId, "params"), "http://localhost/tasks");
        Assert.assertThat(resource.getProperties().get("href"), is("http://localhost/tasks/someId"));
        verify(taskRunnerMock).asyncExec(task);
    }

}
