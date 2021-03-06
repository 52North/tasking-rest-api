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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.n52.tasking.data.ParseValueException;
import org.n52.tasking.data.ServiceProviderInterfaceException;
import org.n52.tasking.data.SimpleTextDecoder;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.sml.device.SmlDevice;
import org.n52.tasking.data.sml.device.SmlWriter;
import org.n52.tasking.data.sml.xml.XPathSmlParser;
import org.n52.tasking.data.sml.xml.XPathSmlWriter;
import org.n52.tasking.data.sml.xml.ParseException;
import org.n52.tasking.data.sml.xml.XPathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class SmlFileConfigWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmlFileConfigWriter.class);

    private final SmlDevice smlDevice;

    public SmlFileConfigWriter(SmlDevice smlDevice) {
        this.smlDevice = smlDevice;
    }

    public void saveConfiguration(String configParameters) throws ParseValueException, ServiceProviderInterfaceException {
        overrideConfig(updateSml(configParameters));
    }

    protected void overrideConfig(Document document) {
        File file = smlDevice.getSmlConfigFile();
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
            StreamResult streamResult = new StreamResult(os);
            Transformer transformer = createXmlTransformer();
            transformer.transform(new DOMSource(document), streamResult);
        } catch (IOException | TransformerException e) {
            LOGGER.error("Could not override SML file '{}'.", file.getAbsolutePath(), e);
        }
    }

    private Transformer createXmlTransformer() throws TransformerConfigurationException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    private Document updateSml(String configParameters) throws ParseValueException, ServiceProviderInterfaceException {
        Device device = smlDevice.getDevice();
        SimpleTextDecoder decoder = new SimpleTextDecoder(getConfigDescription(device));
        try {
            XPathParser xpathParser = new XPathParser(smlDevice.getSmlConfigFile());
            SmlWriter smlWriter = new XPathSmlWriter(new XPathSmlParser(xpathParser));
            decoder.decode(configParameters).stream().forEach((parameter) -> {
                smlWriter.setParameterValue(parameter);
            });
            return smlWriter.getDocument();
        } catch (ParseException e) {
            LOGGER.error("Could not parsse sensor configuration", e);
            throw new ServiceProviderInterfaceException("Unable to perform configuration task.");
        }
    }

    private TaskingDescription getConfigDescription(Device device) {
        return device.getTaskingDescriptions().get(0);
    }

}
