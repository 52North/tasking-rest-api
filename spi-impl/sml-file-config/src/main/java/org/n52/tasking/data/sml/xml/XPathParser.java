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
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XPathParser.class);

    private final Document document;

    public XPathParser(File file) throws ParseException {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            this.document = domFactory.newDocumentBuilder().parse(file);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException("Could not parse XML from '" + file.getAbsolutePath() + "'", e);
        }
    }

    public Node parseNode(String expression) {
        LOGGER.trace("parsing node via '{}'", expression);
        return (Node) evaluate(expression, XPathConstants.NODE);
    }

    public Node parseNode(String expression, Node node) {
        LOGGER.trace("parsing node via '{}' at {}", expression, node);
        return (Node) evaluate(expression, node, XPathConstants.NODE);
    }

    public NodeList parseNodes(String expression) {
        LOGGER.trace("parsing node list via '{}'", expression);
        return (NodeList) evaluate(expression, XPathConstants.NODESET);
    }

    public NodeList parseNodes(String expression, Node node) {
        LOGGER.trace("parsing node list via '{}' at {}", expression, node);
        return (NodeList) evaluate(expression, node, XPathConstants.NODESET);
    }

    public String parseString(String expression) {
        LOGGER.trace("parsing string via '{}'", expression);
        return (String) evaluate(expression, XPathConstants.STRING);
    }

    public String parseString(String expression, Node node) {
        LOGGER.trace("parsing string via '{}' at {}", expression, node);
        return (String) evaluate(expression, node, XPathConstants.STRING);
    }

    public boolean parseBoolean(String expression) {
        LOGGER.trace("parsing boolean via '{}'", expression);
        return (boolean) evaluate(expression, XPathConstants.BOOLEAN);
    }

    public boolean parseBoolean(String expression, Node node) {
        LOGGER.trace("parsing boolean via '{}' at {}", expression, node);
        return (boolean) evaluate(expression, node, XPathConstants.BOOLEAN);
    }

    private Object evaluate(String expression, QName returnType) {
        return evaluate(expression, document, returnType);
    }

    private Object evaluate(String expression, Object input, QName returnType) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            return xpath.evaluate(expression, input, returnType);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Illegal XPath expression: '" + expression + "'", e);
        }
    }

    private XPathExpression compileXPathExpression(String expression) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        return xpath.compile(expression);
    }

}
