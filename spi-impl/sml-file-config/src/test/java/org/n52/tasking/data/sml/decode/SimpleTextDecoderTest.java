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
package org.n52.tasking.data.sml.decode;

import org.n52.tasking.data.sml.decode.SimpleTextDecoder;
import java.util.List;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.Test;
import org.n52.tasking.data.ParseValueException;
import org.n52.tasking.data.entity.CountParameter;
import org.n52.tasking.data.entity.Parameter;
import org.n52.tasking.data.entity.QuantityParameter;
import org.n52.tasking.data.entity.TaskingDescription;
import org.n52.tasking.data.entity.TextParameter;

public class SimpleTextDecoderTest {

    @Test
    public void when_values_then_notEmptyParameters() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "10.5";
        List<Parameter<?>> decodedParameters = simpleTextDecoder.decode(encodedParameters);
        assertThat(decodedParameters, is(not(empty())));
    }

    @Test
    public void when_differentParameterTypes_then_matchingDecodedTypes() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        description.addParameter(new TextParameter("purpose"));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "10.5,Just for testing purposes";
        List<Parameter<?>> decodedParameters = simpleTextDecoder.decode(encodedParameters);
        MatcherAssert.assertThat(decodedParameters.get(0).getType(), Matchers.is("quantity"));
        assertThat(decodedParameters.get(1).getType(), is("text"));
    }

    @Test
    public void when_differentParameterTypes_then_matchingDecodedValues() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        description.addParameter(new TextParameter("purpose"));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "10.5,Just for testing purposes";
        List<Parameter<?>> decodedParameters = simpleTextDecoder.decode(encodedParameters);
        assertThat(decodedParameters.get(0).getValue(), is(10.5));
        assertThat(decodedParameters.get(1).getValue(), is("Just for testing purposes"));
    }

    @Test
    public void when_nonPresentOptional_then_notPresentInDecodedValues() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        description.addParameter(new TextParameter("purpose", true));
        description.addParameter(new CountParameter("count"));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "10.5,N,20";
        List<Parameter<?>> decodedParameters = simpleTextDecoder.decode(encodedParameters);
        assertThat(decodedParameters.size(), is(2));
        assertThat(decodedParameters.get(1).getValue(), is(20));
    }

    @Test
    public void when_presentOptional_then_presentInDecodedValues() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        description.addParameter(new TextParameter("purpose", true));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "10.5,Y,Just for testing purposes";
        List<Parameter<?>> decodedParameters = simpleTextDecoder.decode(encodedParameters);
        assertThat(decodedParameters.size(), is(2));
        assertThat(decodedParameters.get(1).getValue(), is("Just for testing purposes"));
    }

    @Test(expected = ParseValueException.class)
    public void when_illegalValue_then_throwException() throws ParseValueException {
        TaskingDescription description = new TaskingDescription("");
        description.addParameter(new QuantityParameter("frequency"));
        SimpleTextDecoder simpleTextDecoder = new SimpleTextDecoder(description);

        String encodedParameters = "Just for testing purposes";
        simpleTextDecoder.decode(encodedParameters);
    }
}
