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

import org.n52.tasking.data.sml.xml.XPathSmlWriter;
import org.n52.tasking.data.sml.xml.XPathSmlParser;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.n52.tasking.data.entity.Parameter;
import org.n52.tasking.data.sml.xml.ParseException;
import org.n52.tasking.data.sml.xml.XPathParser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XPathSmlWriterTest {

    private static final String LISA_INSTANCE_FILE = "sml/lisa-instance.xml";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testFile;

    @Before
    public void setUp() throws Exception {
        Path path = Paths.get(getClass().getResource("/").toURI());
        File source = path.resolve(LISA_INSTANCE_FILE).toFile();
        testFile = tempFolder.newFile("lisa-instance.xml");
        FileUtils.copyFile(source, testFile);
    }

    @Test
    public void when_updatedSmlIsWritten_then_updatesPresentInDOM() throws ParseException {
        final XPathSmlParser smlParser = new XPathSmlParser(new XPathParser(testFile));
        final String parameterName = "measurementInterval";
        XPathSmlWriter writer = new XPathSmlWriter(smlParser);
        writer.setParameterValue(parameterName, 20.4);
        List<Parameter<?>> parameters = smlParser.getUpdatableParameters();
        final Optional<Parameter<?>> parameter = parameters.stream()
                .filter(p -> p.getName().equals(parameterName))
                .findFirst();
        assertThat("Parameter '" + parameterName + "' not found", parameter.isPresent(), is(true));
        assertThat(parameter.get().getValue(), is(20.4));
    }

}
