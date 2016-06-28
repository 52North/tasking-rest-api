package org.n52.tasking.data.sml.device;

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
import org.n52.tasking.data.sml.ParseException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.n52.tasking.data.RepositoryConfigurationException;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmlConfigDeviceRepository implements DeviceRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger((SmlConfigDeviceRepository.class));

    private final Map<String,Device> id2Device = new HashMap<>();

    private boolean failOnParsingErrors = false;

    public SmlConfigDeviceRepository(String smlFolder) throws RepositoryConfigurationException {
        File folder = new File(smlFolder);
        if ( !isValid(folder)) {
            throw new RepositoryConfigurationException("SML config folder does not exist: '" + folder.toString() + "'");
        }
        readDevices(folder);
    }

    private static boolean isValid(File folder) {
        return folder.exists()
               && folder.isDirectory();
    }

    private void readDevices(File folder) throws RepositoryConfigurationException {
        for (File file : folder.listFiles()) {
            parseToDevice(file);
        }
    }

    private void parseToDevice(File file) throws RepositoryConfigurationException {
        String extension = FilenameUtils.getExtension(file.getName());
        if ("xml".equalsIgnoreCase(extension)) {
            parsePlainXmlToDevice(file);
        }
    }

    private void parsePlainXmlToDevice(File file) throws RepositoryConfigurationException {
        try {
            DeviceParser parser = new DeviceParser(file);
            Device device = parser.parse();
            id2Device.put(device.getId(), device);
        } catch (ParseException e) {
            String filePath = file.getAbsolutePath();
            if (failOnParsingErrors) {
                throw new RepositoryConfigurationException("Could not parse file '" + filePath + "'.", e);
            } else {
                LOGGER.info("Device parsing failed for file '" + filePath);
            }
        }
    }

    @Override
    public List<Device> getDevices() {
        ArrayList<Device> devices = new ArrayList<>(id2Device.values());
        return Collections.unmodifiableList(devices);
    }

    @Override
    public boolean hasDevice(String id) {
        return id2Device.containsKey(id);
    }

    @Override
    public Device getDevice(String id) {
        return null;
    }

    public void setFailOnParsingErrors(boolean failOnParsingErrors) {
        this.failOnParsingErrors = failOnParsingErrors;
    }

}
