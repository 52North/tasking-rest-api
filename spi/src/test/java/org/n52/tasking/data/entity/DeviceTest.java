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
package org.n52.tasking.data.entity;

import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class DeviceTest {

    @Test
    public void when_multipleDescriptions_then_findTaskDescriptionViaEncodedParameter() {
        Device device = new Device("domainId", "label", "some description");
        TaskingDescription description1 = device.addNewTaskingDescription("example1")
                .addParameter(new QuantityParameter("frequency"))
                .addParameter(new TextParameter("purpose"));
        TaskingDescription description2 = device.addNewTaskingDescription("example2")
                .addParameter(new QuantityParameter("foobar"))
                .addParameter(new TextParameter("barfoo"));
        final Optional<TaskingDescription> found = device.findTaskingDescriptionBy("example2,some,params");
        assertThat(found.get(), is(description2));
    }

    @Test
    public void when_multipleDescriptionsAndNoTaskingNamePresent_then_firstTaskingDescription() {
        Device device = new Device("domainId", "label", "some description");
        TaskingDescription description1 = device.addNewTaskingDescription("example1")
                .addParameter(new QuantityParameter("frequency"))
                .addParameter(new TextParameter("purpose"));
        TaskingDescription description2 = device.addNewTaskingDescription("example2")
                .addParameter(new QuantityParameter("foobar"))
                .addParameter(new TextParameter("barfoo"));
        final Optional<TaskingDescription> found = device.findTaskingDescriptionBy("some,params");
        assertThat(found.get(), is(description1));
    }

    @Test
    public void when_singleDescription_then_firstTaskingDescription() {
        Device device = new Device("domainId", "label", "some description");
        TaskingDescription description1 = device.addNewTaskingDescription("example1")
                .addParameter(new QuantityParameter("frequency"))
                .addParameter(new TextParameter("purpose"));
        final Optional<TaskingDescription> found = device.findTaskingDescriptionBy("some,params");
        assertThat(found.get(), is(description1));
    }

}
