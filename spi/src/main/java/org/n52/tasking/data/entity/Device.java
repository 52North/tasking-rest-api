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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.n52.tasking.data.InputValidationException;
import org.n52.tasking.data.SimpleTextValidator;

public class Device {

    private final SimpleTextValidator validator;

    private String id;

    private String domainId;
    private String label;
    private String description;

    private DeviceDescriptionData descriptionData;

    private Offering offering;

    private String phenomenon;

    private String feature;

    private List<TaskingDescription> taskingDescriptions;

    public Device(String domainId, String label, String description) {
        this.domainId = domainId;
        this.label = label;
        this.description = description;
        this.taskingDescriptions = new ArrayList<>();
        this.validator = new SimpleTextValidator(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String storageId) {
        this.id = storageId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DeviceDescriptionData getDescriptionData() {
        return descriptionData;
    }

    public void setDescriptionData(DeviceDescriptionData descriptionData) {
        this.descriptionData = descriptionData;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public void validate(String inputParameters) throws InputValidationException {
        this.validator.validate(inputParameters);
    }

    public TaskingDescription addNewTaskingDescription(String name) {
        TaskingDescription taskingDescription = new TaskingDescription(name);
        taskingDescriptions.add(taskingDescription);
        return taskingDescription;
    }

    public Optional<TaskingDescription> findTaskingDescriptionBy(String parameters) {
        Optional<TaskingDescription> foundDescription = taskingDescriptions.stream()
                .filter(d ->  parameters.startsWith(d.getName()))
                .findFirst();
        return !foundDescription.isPresent()
                ? Optional.of(taskingDescriptions.get(0))
                : foundDescription;
    }

    public List<TaskingDescription> getTaskingDescriptions() {
        return Collections.unmodifiableList(taskingDescriptions);
    }

    public void setTaskingDescriptions(List<TaskingDescription> taskingDescriptions) {
        this.taskingDescriptions = taskingDescriptions;
    }

    @Override
    public String toString() {
        return "Device{" + "domainId=" + domainId + ", label=" + label + ", description=" + description + ", descriptionData=" + descriptionData + ", offering=" + offering + ", phenomenon=" + phenomenon + ", feature=" + feature + ", taskingDescriptions=" + taskingDescriptions + '}';
    }

}
