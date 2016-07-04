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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.n52.tasking.data.InputValidationException;
import org.n52.tasking.data.TaskRunner;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.DeviceRepository;
import org.n52.tasking.data.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE_TIME;

    private final TaskRunner taskRunner;

    private DeviceRepository deviceRepository;

    private TaskRepository taskRepository;

    public TaskService(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public List<Resource> getTasks(String fullUrl) {
        final Function<Task, Resource> toResource =  dm
                -> Resource.aResource(dm.getId())
                .withHref(createHref(fullUrl, dm.getId()));

        return this.taskRepository.getTasks()
                .stream()
                .map(toResource)
                .collect(Collectors.toList());
    }

    public Resource getTask(String id) throws UnknownItemException {
        if (!this.taskRepository.hasTask(id)) {
            throw new UnknownItemException("Not found");
        }
        Task task = this.taskRepository.getTask(id);
        return Resource.aResource(task.getId())
                .withProperty("taskStatus", task.getTaskStatus())
                .withProperty("updatedAt", format(task.getUpdatedAt()))
                .withProperty("submittedAt", format(task.getSubmittedAt()))
                .withProperty("encodedParameters", task.getEncodedParameters().get());
    }

    public Resource createTask(CreateTask createTask, String fullUrl) throws UnknownItemException, InputValidationException {
        Device device = validateInput(createTask);
        Task task = taskRepository.createTask(device, createTask);
        taskRunner.asyncExec(task);

        return Resource.aResource(task.getId())
                .withProperty("taskStatus", task.getTaskStatus())
                .withProperty("updatedAt", format(task.getUpdatedAt()))
                .withProperty("submittedAt", format(task.getSubmittedAt()))
                .withHref(createHref(fullUrl, task.getId()));
    }

    protected Device validateInput(CreateTask createTask) throws InputValidationException, UnknownItemException {
        final String deviceId = createTask.getId();
        if ( !this.deviceRepository.hasDevice(deviceId)) {
            throw new UnknownItemException("Device does not exist: '" + deviceId + "'.");
        }
        Device device = deviceRepository.getDevice(deviceId);
        device.validate(createTask.getParameters());
        return device;
    }

    private String format(LocalDateTime dateTime) {
        return dateTime != null
                ? dateFormatter.format(dateTime)
                : null;
    }

    private String createHref(String fullUrl, String id) {
        return String.format("%s/%s", fullUrl, id);
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat != null) {
            try {
                this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
            } catch (IllegalArgumentException e) {
                LOGGER.info("Illegal dateFormat: '{}'", dateFormat, e);
            }
        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

}
