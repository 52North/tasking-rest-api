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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.n52.tasking.data.entity.BooleanParameter;
import org.n52.tasking.data.entity.CountParameter;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.DeviceDescriptionData;
import org.n52.tasking.data.entity.QuantityParameter;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.entity.TextParameter;
import org.n52.tasking.data.sml.xml.ParseException;
import org.n52.tasking.data.sml.xml.XPathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class DeviceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceParser.class);

    private static final String CONFIGURATION_TASKING = "configurationTask";

    private static final String XPATH_PROPERTIES_FILE = "xpath.properties";

    private final String sensorMLType;
    
    private final Properties xpaths;

    private final XPathParser parser;
    
    DeviceParser(File file) throws ParseException {
        parser = new XPathParser(file);
        sensorMLType = getSensorMLType();
        xpaths = readXPathProperties();
    }

    private String getSensorMLType() {
        Node type = getNextTagNode(parser.parseNode("/"));
        return getPrefixStrippedName(type).toLowerCase();
    }

    private Properties readXPathProperties() {
        Properties properties = new Properties();
        try {
            File file = getXPathPropertiesFile();
            properties.load(new FileReader(file));
            return properties;
        } catch(IOException | URISyntaxException e) {
            LOGGER.error("Could not read XPath properties: '{}'", XPATH_PROPERTIES_FILE, e);
            return properties;
        }
    }

    protected File getXPathPropertiesFile() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("/").toURI());
        return path.resolve(XPATH_PROPERTIES_FILE).toFile();
    }

    public Device parse() {
        Device device = parseDevice();
        parseDescriptionData(device);
        parseTaskingParameters(device);
        LOGGER.debug("Parsed device: {}", device.toString());
        return device;
    }
    
    private String getXPath(String key) {
        return xpaths.getProperty(sensorMLType + "." + key);
    }

    private Device parseDevice() {
        String id = parser.parseString(getXPath("identifier.string"));
        String description = parser.parseString(getXPath("description.string"));
        String label = parser.parseString(getXPath("label.string"));
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
        NodeList nodes = parser.parseNodes(getXPath("updatableParameters.nodes"));
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
