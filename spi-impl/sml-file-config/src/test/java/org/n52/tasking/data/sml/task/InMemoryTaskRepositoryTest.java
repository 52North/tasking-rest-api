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

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InMemoryTaskRepositoryTest {

    private TaskRepository taskRepository;

    @Before
    public void setUp() {
        taskRepository = new InMemoryTaskRepository();
    }

    @Test
    public void when_emptyRepository_then_emptyList() {
        assertTrue(taskRepository.getTasks().isEmpty());
    }

    @Test
    public void when_addingTask_then_expectNonNullTask() {
        CreateTask cmd = new CreateTask("42", "some-fancy-parameters-here");
        Device device = new Device("domainId", "label", "description");
        assertNotNull(taskRepository.createTask(device, cmd));
    }

    @Test
    public void when_addingTask_then_expectTaskDeviceRelation() {
        Device device = new Device("domainId", "label", "description");
        final CreateTask createTask = new CreateTask(device.getId(), "params");
        Task task = taskRepository.createTask(device, createTask);
        assertFalse(taskRepository.getTasks(device.getId()).isEmpty());
        assertThat(task.getDeviceId(), CoreMatchers.is(device.getId()));
    }

}
