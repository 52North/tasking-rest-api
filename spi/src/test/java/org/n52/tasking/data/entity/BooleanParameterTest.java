package org.n52.tasking.data.entity;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.n52.tasking.data.ParseValueException;

public class BooleanParameterTest {

    @Test
    public void when_parsedValueOnTemplate_then_newValueInstance() throws ParseValueException {
        Parameter<Boolean> parameter = new BooleanParameter("test", false);
        final Parameter<Boolean> instance = parameter.toValueInstance("y");
        MatcherAssert.assertThat(instance.getValue(), Matchers.is(true));
    }
}
