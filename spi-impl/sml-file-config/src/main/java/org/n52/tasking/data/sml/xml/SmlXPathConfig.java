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
package org.n52.tasking.data.sml.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmlXPathConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmlXPathConfig.class);

    private static final String XPATH_PROPERTIES_FILE = "xpath.properties";

    private final String smlType;

    private File propertiesFile;

    private Properties xpaths;

    public SmlXPathConfig(String smlTypePrefix) {
        this.smlType = prepare(smlTypePrefix);
    }

    private static String prepare(String smlTypePrefix) {
        String prefix = !smlTypePrefix.endsWith(".")
                ? smlTypePrefix + "."
                : smlTypePrefix;
        return prefix.toLowerCase();
    }

    public String getXPath(String key, String... properties) {
        if (xpaths == null) {
            xpaths = loadXPathProperties();
        }
        final String xpath = xpaths.getProperty(smlType + key);
        return MessageFormat.format(xpath, (Object[]) properties);
    }

    private Properties loadXPathProperties() {
        Properties properties = new Properties();
        try {
            File file = propertiesFile == null
                    ? getDefaultXPathPropertiesFile()
                    : propertiesFile;
            properties.load(new FileReader(file));
            return properties;
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Could not read XPath properties: '{}'", XPATH_PROPERTIES_FILE, e);
            return properties;
        }
    }

    private File getDefaultXPathPropertiesFile() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("/").toURI());
        return path.resolve(XPATH_PROPERTIES_FILE).toFile();
    }

    public void setXPathPropertiesFile(File file) {
        if (file != null && file.exists()) {
            this.propertiesFile = file;
        } else {
            LOGGER.warn("Could not find file '{}'", file != null
                    ? file.getAbsolutePath()
                    : "null");
        }
    }

}
