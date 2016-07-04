package org.n52.tasking.core.service;

import java.util.Collections;
import java.util.List;

public class ResultService {
    
    public List<Resource> getResults(String fullUrl) {
        return Collections.emptyList();
    }

    public Resource getResult(String id) throws UnknownItemException {
        throw new UnknownItemException("Result '" + id + "' not found!");
    }
}
