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

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.n52.tasking.data.entity.BooleanParameter;
import org.n52.tasking.data.entity.CountParameter;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.DeviceDescriptionData;
import org.n52.tasking.data.entity.Parameter;
import org.n52.tasking.data.entity.QuantityParameter;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.entity.TextParameter;
import org.n52.tasking.data.sml.ParseException;
import org.n52.tasking.data.sml.XPathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DeviceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceParser.class);

    private static final String CONFIGURATION_TASKING = "configurationTask";

    private final XPathParser parser;

    DeviceParser(File file) throws ParseException {
        try {
            parser = new XPathParser(file);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException("Unable to create XPathParser", e);
        }
    }

    public Device parse() {
        LOGGER.debug("Parsing device ...");
        Device device = parseDevice();
        parserDescriptionData(device);
        parseTaskingParameters(device);
        LOGGER.debug("Parsed device: {}", device.toString());
        return device;
    }

    private Device parseDevice() {
        String id = parser.parseString("/PhysicalComponent/identifier");
        String description = parser.parseString("/PhysicalComponent/description");
        String label = parser.parseString("/PhysicalComponent/identification/IdentifierList/identifier/Term[contains(@definition,'#modelID')]/value/text()");
        return new Device(id, label, description);
    }

    private void parserDescriptionData(Device device) {

        // TODO

        // add dummy
        DeviceDescriptionData deviceDescriptionData = new DeviceDescriptionData();
        deviceDescriptionData.setFormat("http://www.opengis.net/sensorML/1.0.1");
        deviceDescriptionData.setHref("http://colabis.52north.org/sps-2.0/sml/CiteTestSensor/1.xml");
        device.setDescriptionData(deviceDescriptionData);
    }

    private void parseTaskingParameters(Device device) {
        TaskingDescription taskDescription = device.addNewTaskingDescription(CONFIGURATION_TASKING);
        NodeList nodes = parser.parseNodes("/PhysicalComponent/parameters/ParameterList/parameter[@updatable='true']");
        for (int i = 0 ; i < nodes.getLength() ; i++) {
            Node item = nodes.item(i);
            Node parameterType = getNextTagNode(item);
            String name = parser.parseString("@name", item);
            boolean optional = parser.parseBoolean("@optional", item);
            String nodeName = getPrefixStrippedName(parameterType);
            if (nodeName.equalsIgnoreCase("quantity")) {
                QuantityParameter parameter = new QuantityParameter(name);
                parameter.setOptional(optional);
                taskDescription.addParameter(parameter);
            } else if (nodeName.equalsIgnoreCase("boolean")) {
                BooleanParameter parameter = new BooleanParameter(name);
                parameter.setOptional(optional);
                taskDescription.addParameter(parameter);
            } else if (nodeName.equalsIgnoreCase("count")) {
                CountParameter parameter = new CountParameter(name);
                parameter.setOptional(optional);
                taskDescription.addParameter(parameter);
            } else if (nodeName.equalsIgnoreCase("text")) {
                TextParameter parameter = new TextParameter(name);
                parameter.setOptional(optional);
                taskDescription.addParameter(parameter);
            }
        }
    }

    private Node getNextTagNode(Node node) {
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength() ; i++) {
            final Node child = nodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return child;
            }
        }
        return null;
    }

    private String getPrefixStrippedName(Node parameterType) {
        final String nodeName = parameterType.getNodeName();
        return nodeName.substring(nodeName.indexOf(":") + 1);
    }

}
