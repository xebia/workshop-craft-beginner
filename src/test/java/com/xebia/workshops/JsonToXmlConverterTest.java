package com.xebia.workshops;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonToXmlConverterTest {

    @Test
    public void shouldUseDefaultNameForTopLevelEmptyArray() {
        JsonToXmlConverter converter = new JsonToXmlConverter();

        assertThat(converter.convert("[]"), is("<array/>"));
    }

    @Test
    public void shouldUseDefaultNamesForTopLevelSimpleArray() {
        JsonToXmlConverter converter = new JsonToXmlConverter();

        assertThat(converter.convert("[1,2,3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldUseDefaultNameForTopLevelEmptyObject() {
        JsonToXmlConverter converter = new JsonToXmlConverter();

        assertThat(converter.convert("{}"), is("<object/>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithSingleStringField() {
        JsonToXmlConverter converter = new JsonToXmlConverter();

        assertThat(converter.convert("{\"field1\":\"value1\"}"), is("<object><field1>value1</field1></object>"));
    }
}
