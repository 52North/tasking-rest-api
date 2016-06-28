package org.n52.tasking.data.entity;

public class QuantityParameter extends Parameter {
    
    private String uom;
    
    // TODO allowedValues

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
    
}
