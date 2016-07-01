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
package org.n52.tasking.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.n52.tasking.data.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public abstract Runnable getRunnable(Task task);

    public final void asyncExec(Task task) {
        task.setSubmittedAt(LocalDateTime.now(ZoneId.of("Z")));
        task.setTaskStatus(TaskStatus.RUNNING.name());
        task.setPercentCompletion(0.0);
        getExecutorService().execute(getRunnable(task));
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void shutdown() {
        LOGGER.info("Shut down task runner. Stopping all running tasks.");
        executorService.shutdownNow();
    }
}
