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
import org.n52.tasking.data.RepositoryConfigurationException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.n52.tasking.data.entity.Device;

public class SmlConfigDeviceRepositoryTest {

    private static final String SML_CONFIG_FOLDER = "/files";

    private static final String LISA_INSTANCE_ID = "http://www.nexosproject.eu/resource/procedure/trios/lisa/1234567890";
    
    private SmlConfigDeviceRepository repository;

    @Before
    public void setUp() throws Exception {
        File folder = getTestConfigFolder().toFile();
        repository = new SmlConfigDeviceRepository(folder.getAbsolutePath());
    }

    @Test(expected = RepositoryConfigurationException.class)
    public void when_configFolderNotPresentOnCreation_thenException() throws Exception {
        repository = new SmlConfigDeviceRepository("does-not-exist");
    }

    @Test(expected = RepositoryConfigurationException.class)
    public void when_givenFileOnCreation_thenException() throws Exception {
        String file = getTestConfigFile("empty.txt").getAbsolutePath();
        repository = new SmlConfigDeviceRepository(file);
    }

    @Test
    public void when_xmlAvailable_then_notEmptyCollection() {
        assertThat(repository.getDevices(), is(not(empty())));
    }

    @Test
    public void when_configFolderRead_then_lisaInstanceAvailable() {
        assertTrue(repository.hasDevice(LISA_INSTANCE_ID));
    }
    
    @Test
    public void when_lisaInstanceAvailable_then_notEmptyUpdatableParameters() {
        Device device = repository.getDevice(LISA_INSTANCE_ID);
        assertTrue(device.getDescriptionData());
    }

    private Path getTestConfigFolder() throws URISyntaxException {
        return Paths.get(getClass().getResource(SML_CONFIG_FOLDER).toURI());
    }


    private File getTestConfigFile(String name) throws URISyntaxException {
        Path folder = getTestConfigFolder();
        return folder.resolve(name).toFile();
    }

}
