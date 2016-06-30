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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.n52.tasking.data.RepositoryConfigurationException;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmlConfigDeviceRepository implements DeviceRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger((SmlConfigDeviceRepository.class));

    private final Map<String, SmlDevice> deviceById = new HashMap<>();

    private boolean failOnParsingErrors = false;

    private Path basePath;

    public SmlConfigDeviceRepository(String smlFolder) throws RepositoryConfigurationException {
        File folder = getFolder(smlFolder);
        if (!isValid(folder)) {
            throw new RepositoryConfigurationException("SML config folder does not exist: '" + folder.toString() + "'");
        }
        LOGGER.info("Reading sml devices from '{}' ...", folder.getAbsolutePath());
        readDevices(folder);
    }

    private boolean isValid(File folder) {
        return folder.isDirectory();
    }

    private File getFolder(String smlFolder) throws RepositoryConfigurationException {
        try {
            final Path root = basePath == null
                    ? Paths.get(getClass().getResource("/").toURI())
                    : basePath;
            Path target = root.resolve(smlFolder);
            return target.toFile();
        } catch (URISyntaxException e) {
            throw new RepositoryConfigurationException("Could not resolve SML folder: '" + smlFolder + "'", e);
        }
    }

    private void readDevices(File folder) throws RepositoryConfigurationException {
        for (File file : folder.listFiles()) {
            parseToDevice(file);
        }
    }

    private void parseToDevice(File file) throws RepositoryConfigurationException {
        int count = 0;
        String extension = FilenameUtils.getExtension(file.getName());
        if ("xml".equalsIgnoreCase(extension)) {
            parsePlainXmlToDevice(file);
            count++;
        }
        LOGGER.info("Parsed #{} device files", count);
    }

    private void parsePlainXmlToDevice(File file) throws RepositoryConfigurationException {
        try {
            DeviceParser parser = new DeviceParser(file);
            Device device = parser.parse();
            StorageIdGenerator.generateIdFor(device);
            deviceById.put(device.getId(), new SmlDevice(device, file));
        } catch (ParseException e) {
            String filePath = file.getAbsolutePath();
            if (failOnParsingErrors) {
                throw new RepositoryConfigurationException("Could not parse file '" + filePath + "'.", e);
            } else {
                LOGGER.info("Parsing device failed for file '{}'", filePath);
            }
        }
    }

    @Override
    public List<Device> getDevices() {
        ArrayList<SmlDevice> devices = new ArrayList<>(deviceById.values());
        return Collections.unmodifiableList(devices
                .stream()
                .map(d -> d.getDevice())
                .collect(Collectors.toList())
        );
    }

    @Override
    public boolean hasDevice(String id) {
        return deviceById.containsKey(id);
    }

    @Override
    public Device getDevice(String id) {
        return deviceById.get(id).getDevice();
    }

    public void setFailOnParsingErrors(boolean failOnParsingErrors) {
        this.failOnParsingErrors = failOnParsingErrors;
    }

    private static class StorageIdGenerator {
        static void generateIdFor(Device device) {
            final byte[] bytes = device.getDomainId().getBytes();
            device.setId(UUID.nameUUIDFromBytes(bytes).toString());
        }
    }

}
