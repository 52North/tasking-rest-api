package org.n52.tasking.data.entity;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class TaskDescriptionTest {
    
    @Test
    public void when_addingParameters_then_keepOrdering() {
        TaskingDescription description = new TaskingDescription();
        description.addParameter(new CountParameter("first"));
        description.addParameter(new TextParameter("second"));
        description.addParameter(new TextParameter("third"));
        List<Parameter> parameters = description.getTaskingParameters();
        Assert.assertThat(parameters.get(0).getName(), Matchers.is("first"));
        Assert.assertThat(parameters.get(1).getName(), Matchers.is("second"));
        Assert.assertThat(parameters.get(2).getName(), Matchers.is("third"));
    }
}
