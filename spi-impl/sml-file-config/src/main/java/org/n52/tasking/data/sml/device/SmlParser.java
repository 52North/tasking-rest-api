package org.n52.tasking.data.sml.device;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.n52.tasking.data.entity.BooleanParameter;
import org.n52.tasking.data.entity.CountParameter;
import org.n52.tasking.data.entity.Parameter;
import org.n52.tasking.data.entity.QuantityParameter;
import org.n52.tasking.data.entity.TextParameter;
import org.n52.tasking.data.sml.xml.SmlXPathConfig;
import org.n52.tasking.data.sml.xml.XPathParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SmlParser {

    private final XPathParser xPathParser;

    private final SmlXPathConfig xpathConfig;

    public SmlParser(XPathParser parser) {
        this.xPathParser = parser;
        this.xpathConfig = new SmlXPathConfig(getSensorMLType());
    }

    private String getSensorMLType() {
        Node type = getNextTagNode(xPathParser.parseNode("/"));
        return getPrefixStrippedName(type).toLowerCase();
    }


    void setXPathConfigFile(File file) {
        this.xpathConfig.setXPathPropertiesFile(file);
    }

    String get(String key) {
        return xPathParser.parseString(xpathConfig.getXPath(key));
    }

    String getIdentifier() {
        return get("identifier.string");
    }

    String getDescription() {
        return get("description.string");
    }

    String getLabel() {
        return get("label.string");
    }

    List<Parameter<?>> getUpdatableParameters() {
        List<Parameter<?>> parameters = new ArrayList<>();
        NodeList nodes = xPathParser.parseNodes(xpathConfig.getXPath("updatableParameters.nodes"));
        for (int i = 0 ; i < nodes.getLength() ; i++) {
            Node item = nodes.item(i);
            Node parameterType = getNextTagNode(item);
            String name = xPathParser.parseString("@name", item);
            boolean optional = xPathParser.parseBoolean("@optional", item);
            String nodeName = getPrefixStrippedName(parameterType);
            if (nodeName.equalsIgnoreCase("quantity")) {
                QuantityParameter parameter = new QuantityParameter(name);
                parameter.setOptional(optional);
                parameters.add(parameter);
            } else if (nodeName.equalsIgnoreCase("boolean")) {
                BooleanParameter parameter = new BooleanParameter(name);
                parameter.setOptional(optional);
                parameters.add(parameter);
            } else if (nodeName.equalsIgnoreCase("count")) {
                CountParameter parameter = new CountParameter(name);
                parameter.setOptional(optional);
                parameters.add(parameter);
            } else if (nodeName.equalsIgnoreCase("text")) {
                TextParameter parameter = new TextParameter(name);
                parameter.setOptional(optional);
                parameters.add(parameter);
            }
        }
        return parameters;
    }

    private Node getNextTagNode(Node node) {
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength() ; i++) {
            final Node child = nodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return child;
            }
        }
        return null;
    }

    private String getPrefixStrippedName(Node parameterType) {
        final String nodeName = parameterType.getNodeName();
        return nodeName.substring(nodeName.indexOf(":") + 1);
    }

}
