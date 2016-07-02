package org.n52.tasking.data.sml.device;

import org.n52.tasking.data.entity.Parameter;
import org.w3c.dom.Document;

public interface SmlWriter {

    void setParameterValue(Parameter<?> parameter);
    
    Document getDocument();
}
