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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.NodeList;

public class XPathParserTest {

    private static final String SML_CONFIG_FOLDER = "/sml";

    private XPathParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new XPathParser(getTestConfigFile("lisa-instance.xml"));
    }

    @Test
    public void when_declaringNamespaces_then_expressionsWorkWithPrefixes() throws Exception {
        parser = XPathParser.createWithDefaultNamespaces(getTestConfigFile("lisa-instance.xml"));
        String id = parser.parseString("/sml:PhysicalComponent/gml:identifier");
        assertThat(id, is("http://www.nexosproject.eu/resource/procedure/trios/lisa/1234567890"));
    }

    @Test
    public void when_parserReady_then_parseNodeList() {
        NodeList nodes = parser.parseNodes("/PhysicalComponent/identification");
        assertTrue(nodes.getLength() > 0);
    }

    @Test
    public void when_parserReady_then_parseLisaIdentification() {
        String id = parser.parseString("/PhysicalComponent/identifier");
        assertThat(id, is("http://www.nexosproject.eu/resource/procedure/trios/lisa/1234567890"));
    }

    @Test
    public void when_parserReady_then_parseUpdatableParameters() {
        NodeList nodes = parser.parseNodes("/PhysicalComponent/parameters/ParameterList/parameter[@updatable='true']");
        assertThat(nodes.getLength(), is(4));
    }

    @Test
    public void when_parserReady_then_parseStringFromNode() {
        NodeList nodes = parser.parseNodes("/PhysicalComponent/identification/IdentifierList/identifier");
        String label = parser.parseString("Term/label/text()", nodes.item(0));
        assertThat(label, is("Model ID"));
    }

    private Path getTestConfigFolder() throws URISyntaxException {
        return Paths.get(getClass().getResource(SML_CONFIG_FOLDER).toURI());
    }

    private File getTestConfigFile(String name) throws URISyntaxException {
        Path folder = getTestConfigFolder();
        return folder.resolve(name).toFile();
    }

}
