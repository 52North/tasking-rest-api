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
package org.n52.tasking.data.entity;

import java.time.LocalDateTime;
import java.util.Optional;

public class Task {

    private String id;

    private String deviceId;

    private Optional<String> encodedParameters;

    private long estimatedToC;

    private String taskStatus;

    private LocalDateTime submittedAt;

    private double percentCompletion;

    private LocalDateTime updatedAt;

    private Optional<String> resultId;

    public Task(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean hasEncodedParameters() {
        return encodedParameters.isPresent();
    }

    public Optional<String> getEncodedParameters() {
        return encodedParameters;
    }

    public void setEncodedParameters(String encodedParameters) {
        this.encodedParameters = Optional.of(encodedParameters);
    }

    public long getEstimatedToC() {
        return estimatedToC;
    }

    public void setEstimatedToC(long estimatedToC) {
        this.estimatedToC = estimatedToC;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public double getPercentCompletion() {
        return percentCompletion;
    }

    public void setPercentCompletion(double percentCompletion) {
        this.percentCompletion = percentCompletion;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Optional<String> getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = Optional.of(resultId);
    }


}
