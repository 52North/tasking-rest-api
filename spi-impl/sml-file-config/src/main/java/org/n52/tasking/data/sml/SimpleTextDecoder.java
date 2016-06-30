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
package org.n52.tasking.data.sml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.n52.tasking.data.ParseValueException;
import org.n52.tasking.data.entity.Parameter;
import org.n52.tasking.data.entity.QuantityParameter;
import org.n52.tasking.data.entity.TaskingDescription;

public class SimpleTextDecoder {

    private final TaskingDescription taskingDescription;

    public SimpleTextDecoder(TaskingDescription taskingDescription) {
        this.taskingDescription = taskingDescription;
    }

    List<Parameter<?>> decode(String encodedParameters) throws ParseValueException {
        List<Parameter<?>> decodedParameters = new ArrayList<>();

        String[] parameterTokens = parseParameters(encodedParameters);
        List<Parameter<?>> parameters = taskingDescription.getParameters();
        int valueIdx = 0;
        for (Parameter<?> definition : parameters) {
            String token = parameterTokens[valueIdx];
            if (definition.isOptional()) {
                token = isOptionalValueAvailable(token)
                        ? parameterTokens[++valueIdx] // skip 'Y'
                        : null; // reset token
            }
            ++valueIdx; // next token
            if (token != null) {
                decodedParameters.add(definition.toValueInstance(token));
            }
        }
        return decodedParameters;
    }

    private String[] parseParameters(String configParameters) {
        return Arrays.<String>asList(configParameters.split(","))
                .stream()
                .map(t -> t.trim())
                .toArray(String[]::new);
    }

    private boolean isOptionalValueAvailable(String token) {
        return "Y".equalsIgnoreCase(token);
    }

}
