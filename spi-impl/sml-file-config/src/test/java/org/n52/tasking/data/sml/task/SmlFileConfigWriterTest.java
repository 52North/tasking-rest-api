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
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.n52.tasking.data.sml.device.SmlDevice;
import org.n52.tasking.data.sml.device.SmlParser;
import org.n52.tasking.data.sml.xml.XPathSmlParser;
import org.n52.tasking.data.sml.xml.ParseException;
import org.n52.tasking.data.sml.xml.XPathParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SmlFileConfigWriterTest {

    private static final String LISA_INSTANCE_FILE = "sml/lisa-instance.xml";

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void when_saving_then_xmlGetsOverridden() throws Exception {
        File inputFile = tempFolder.newFile();
        FileUtils.write(inputFile, "<root>someText</root>", Charset.forName("UTF-8"));

        SmlDevice smlDevice = new SmlDevice(null, inputFile);
        XPathParser xPathParser = new XPathParser(inputFile);
        Node root = xPathParser.parseNode("/root");
        Document document = root.getOwnerDocument();
        Node newNode = document.createElement("new");
        newNode.appendChild(document.createTextNode("I am new"));
        root.appendChild(newNode);
        new SmlFileConfigWriter(smlDevice).overrideFile(inputFile, document);
        assertThat(new XPathParser(inputFile).parseString("/root/new/text()"), is("I am new"));
    }

}
