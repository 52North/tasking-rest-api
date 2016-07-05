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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.n52.tasking.data.ParseValueException;
import org.n52.tasking.data.ServiceProviderInterfaceException;
import org.n52.tasking.data.TaskRunner;
import org.n52.tasking.data.TaskStatus;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.repository.DeviceRepository;
import org.n52.tasking.data.sml.device.SmlConfigDeviceRepository;
import org.n52.tasking.data.sml.device.SmlDevice;
import org.n52.tasking.data.sml.xml.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmlFileConfigTaskRunner extends TaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmlFileConfigTaskRunner.class);

    private SmlConfigDeviceRepository deviceRepository;

    @Override
    public Runnable getRunnable(final Task task) {
        return () -> {
            String encodedParameters = task.getEncodedParameters().get();
            LOGGER.debug("Running task '{}' (parameters: '{}')", task.getId(), encodedParameters);

            SmlDevice smlDevice = deviceRepository.getSmlDevice(task.getDeviceId());
            Device device = smlDevice.getDevice();
            Optional<TaskingDescription> description = device.findTaskingDescriptionBy(encodedParameters);
            if (description.isPresent() && task.hasEncodedParameters()) {
                performConfigUpdateTask(task, smlDevice);
            }
        };
    }

    private void performConfigUpdateTask(final Task task, final SmlDevice smlDevice) {
        Device device = smlDevice.getDevice();
        final String parameters = task.getEncodedParameters().get();
//        File backup = createConfigBackup(smlDevice); // TODO rollback old config
        try {
            SmlFileConfigWriter writer = new SmlFileConfigWriter(smlDevice);
            writer.saveConfiguration(parameters);
            task.setTaskStatus(TaskStatus.FINISHED.name());
            task.setPercentCompletion(100.0);
            deviceRepository.updateDevice(smlDevice);
        } catch (ParseException | ParseValueException | ServiceProviderInterfaceException e) {
            task.setTaskStatus(TaskStatus.ERROR.name());
            LOGGER.error("Unable to update config for '{}' and parameters '{}' ",
                    device, parameters, e);
        }
    }

    private File createConfigBackup(final SmlDevice smlDevice) throws IOException {
        File configFile = smlDevice.getSmlConfigFile();
        File backup = configFile.toPath().resolve(".backup").toFile();
        FileUtils.copyFile(configFile, backup);
        return backup;
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(SmlConfigDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

}
