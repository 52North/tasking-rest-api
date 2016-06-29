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

import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.n52.tasking.data.entity.Device;
import org.n52.tasking.data.entity.TaskingDescription;

public class DeviceParserTest extends SmlConfigTest {

    private static final String LISA_INSTANCE_ID = "c599c4ea-08bc-3254-8c16-8381b22ab228";

    @Test
    public void when_configFolderRead_then_lisaInstanceAvailable() {
        assertTrue(repository.hasDevice(LISA_INSTANCE_ID));
    }

    @Test
    public void when_lisaInstanceAvailable_then_notEmptyLabel() {
        Device device = repository.getDevice(LISA_INSTANCE_ID);
        assertThat(device.getLabel(), is("LISA - SAC 254nm"));
    }

    @Test
    public void when_lisaInstanceAvailable_then_notEmptyDescription() {
        Device device = repository.getDevice(LISA_INSTANCE_ID);
        assertThat(device.getDescription(), is("Metadata of a LISA device"));
    }

    @Test
    public void when_lisaInstanceAvailable_then_HavingConfigTaskingDescription() {
        Device device = repository.getDevice(LISA_INSTANCE_ID);
        assertThat(device.getTaskingDescriptions().size(), is(1));
        assertThat(device.getTaskingDescriptions().get("configurationTask"), is(notNullValue()));
    }

    @Test
    public void when_lisaInstanceAvailable_then_havingUpdatableParameters() {
        Device device = repository.getDevice(LISA_INSTANCE_ID);
        TaskingDescription taskDescription = device.getTaskingDescriptions().get("configurationTask");
        assertThat(taskDescription.getTaskingParameters().size(), is(4));
    }

}
