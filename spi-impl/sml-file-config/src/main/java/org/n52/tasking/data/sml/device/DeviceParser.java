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
package org.n52.tasking.data.sml.device;

import org.n52.tasking.data.sml.xml.XPathSmlParser;
import java.io.File;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.DeviceDescriptionData;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.sml.xml.ParseException;
import org.n52.tasking.data.sml.xml.XPathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceParser.class);

    private static final String CONFIGURATION_TASKING = "configurationTask";

    private final SmlParser smlParser;

    public DeviceParser(File file) throws ParseException {
        smlParser = new XPathSmlParser(new XPathParser(file));
    }

    public DeviceParser(SmlParser smlParser) {
        this.smlParser = smlParser;
    }

    public Device parse() {
        Device device = parseDevice();
        parseDescriptionData(device);
        parseTaskingParameters(device);
        LOGGER.debug("Parsed device: {}", device.toString());
        return device;
    }

    private Device parseDevice() {
        String label = smlParser.getLabel();
        String id = smlParser.getIdentifier();
        String description = smlParser.getDescription();
        return new Device(id, label, description);
    }

    private void parseDescriptionData(Device device) {

        // TODO

        // add dummy
        DeviceDescriptionData deviceDescriptionData = new DeviceDescriptionData();
        deviceDescriptionData.setFormat("http://www.opengis.net/sensorML/1.0.1");
        deviceDescriptionData.setHref("http://colabis.52north.org/sps-2.0/sml/CiteTestSensor/1.xml");
        device.setDescriptionData(deviceDescriptionData);
    }

    private void parseTaskingParameters(Device device) {
        TaskingDescription taskDescription = device.addNewTaskingDescription(CONFIGURATION_TASKING);
        taskDescription.addAllParameters(smlParser.getUpdatableParameters());
    }

}
