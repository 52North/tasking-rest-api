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
package org.n52.tasking.data.sml.task;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class InMemoryTaskRepositoryTest {

    private TaskRepository repository;

    private TaskRunner taskRunnerMock;

    @Before
    public void setUp() {
        taskRunnerMock = mock(TaskRunner.class);
        repository = new InMemoryTaskRepository(taskRunnerMock);
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
    public void when_addingTask_then_expectTaskAddedToDevice() {
        final String deviceId = "42";
        repository.createTask(new CreateTask(deviceId, "params"));
        assertFalse(repository.getTasks(deviceId).isEmpty());
    }

    @Test
    public void when_addingTask_then_expectTaskToBeRun() {
        final String deviceId = "42";
        Task task = repository.createTask(new CreateTask(deviceId, "params"));
        verify(taskRunnerMock).runTask(task);
    }

}
