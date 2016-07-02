package org.n52.tasking.data.sml.device;

import java.util.List;
import org.n52.tasking.data.entity.Parameter;

public interface SmlParser {

    String getLabel();

    String getIdentifier();

    String getDescription();

    public List<Parameter<?>> getUpdatableParameters();

}
