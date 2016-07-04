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
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.n52.tasking.data.InputValidationException;
import org.n52.tasking.data.TaskRunner;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.DeviceRepository;
import org.n52.tasking.data.repository.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private TaskRunner taskRunnerMock;

    @Before
    public void setUp() {
        when(taskRunnerMock.getRunnable(any())).thenReturn((Runnable) () -> {
            // do nothing
        });
        taskService = prepareWithMocks(new TaskService(taskRunnerMock));
    }

    private TaskService prepareWithMocks(TaskService service) {
        service.setTaskRepository(taskRepository);
        service.setDeviceRepository(deviceRepository);
        return service;
    }

    @Test(expected = UnknownItemException.class)
    public void when_addingTaskForNonExistingDevice_then_throwException() throws Exception {
        when(deviceRepository.hasDevice("does-not-exist")).thenReturn(false);
        final CreateTask createTask = new CreateTask("does-not-exist", "foobar");
        mockRepositoryTaskFor(createTask);
        this.taskService.createTask(createTask, "http://...");
    }

    @Test
    public void when_addingTask_then_expectTaskToBeRun() throws Exception {
        final String deviceId = "42";
        when(deviceRepository.hasDevice(deviceId)).thenReturn(true);
        final CreateTask createTask = new CreateTask(deviceId, "params");
        Task task = mockRepositoryTaskFor(createTask);

        TaskService service = prepareWithMocks(new TaskServiceSeam(taskRunnerMock));
        Resource resource = service.createTask(createTask, "http://localhost/tasks");
        Assert.assertThat(resource.getProperties().get("href"), is("http://localhost/tasks/" + deviceId));
        verify(taskRunnerMock).asyncExec(task);
    }

    private Task mockRepositoryTaskFor(CreateTask createTask) {
        Task task = new Task(createTask.getId());
        task.setSubmittedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setEncodedParameters(createTask.getParameters());
        when(taskRepository.createTask(any(), eq(createTask))).thenReturn(task);
        return task;
    }

    private static class TaskServiceSeam extends TaskService {

        public TaskServiceSeam(TaskRunner taskRunner) {
            super(taskRunner);
        }

        @Override
        protected Device validateInput(CreateTask createTask) throws InputValidationException, UnknownItemException {
            return new Device("deviceId", "label", "description");
        }

    }

}
