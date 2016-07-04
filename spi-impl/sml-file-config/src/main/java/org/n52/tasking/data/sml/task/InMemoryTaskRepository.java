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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, List<Task>> tasksByDevice;

    public InMemoryTaskRepository() {
        this.tasksByDevice = new HashMap<>();
    }

    @Override
    public Task createTask(CreateTask createTask) {
        Task task = new Task(UUID.randomUUID().toString());
        task.setEncodedParameters(createTask.getParameters());
        addTaskForDevice(createTask.getId(), task);
        return task;
    }

    private void addTaskForDevice(String deviceId, Task task) {
        if ( !tasksByDevice.containsKey(deviceId)) {
            tasksByDevice.put(deviceId, new ArrayList<>());
        }
        tasksByDevice.get(deviceId).add(task);
    }

    @Override
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        tasksByDevice.values().stream().forEach((tasks) -> {
            list.addAll(tasks);
        });
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getTasks(String deviceId) {
        List<Task> list = new ArrayList<>();
        tasksByDevice.get(deviceId).stream().forEach((task) -> {
            list.add(task);
        });
        return Collections.unmodifiableList(list);
    }

    @Override
    public boolean hasTask(String taskId) {
        return getTasks().stream().anyMatch(t -> t.getId().equals(taskId));
    }

    @Override
    public Task getTask(String taskId) {
        return getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .get();
    }

}
