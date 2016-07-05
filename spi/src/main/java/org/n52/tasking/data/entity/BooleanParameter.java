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

import org.n52.tasking.data.ParseValueException;

public class BooleanParameter extends Parameter<Boolean> {

    public BooleanParameter(String name) {
        super(name);
    }

    public BooleanParameter(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public Parameter<Boolean> toValueInstance(String token) throws ParseValueException {
        if (isNullOrEmpty(token)) {
            return this;
        }
        BooleanParameter p = new BooleanParameter(getName(), isOptional());
        p.setValue(parseToken(token));
        return p;
    }

    private Boolean parseToken(String token) throws ParseValueException {
        if ("false".equalsIgnoreCase(token)
                || "f".equalsIgnoreCase(token)
                || "n".equalsIgnoreCase(token)
                || "0".equalsIgnoreCase(token)) {
            return false;
        }
        if ("true".equalsIgnoreCase(token)
                || "t".equalsIgnoreCase(token)
                || "y".equalsIgnoreCase(token)
                || "1".equalsIgnoreCase(token)) {
            return true;
        }
        throw new ParseValueException("No boolean value: '" + token + "'");
    }

    @Override
    public String getType() {
        return "boolean";
    }

}
