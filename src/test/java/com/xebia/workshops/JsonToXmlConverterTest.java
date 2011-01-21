package com.xebia.workshops;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonToXmlConverterTest {
    private JsonToXmlConverter converter = new JsonToXmlConverter();

    // ========================================================================
    // Tests for simple, non-nested JSON
    // ========================================================================

    @Test
    public void shouldUseDefaultNameForTopLevelEmptyArray() {
        assertThat(converter.convert("[]"), is("<array/>"));
    }

    @Test
    public void shouldUseDefaultNamesForTopLevelSimpleArray() {
        assertThat(converter.convert("[1,2,3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldUseDefaultNamesForTopLevelSimpleArrayAndWhitespaceBetweenValues() {
        assertThat(converter.convert("[1, 2, \n 3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldUseDefaultNameForTopLevelEmptyObject() {
        assertThat(converter.convert("{}"), is("<object/>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithSingleStringField() {
        assertThat(converter.convert("{\"field1\":\"value1\"}"), is("<object><field1>value1</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithTwoFields() {
        assertThat(converter.convert("{\"field1\":\"value1\",\"field2\":42}"), is("<object><field1>value1</field1><field2>42</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithFieldValuesContainingColons() {
        assertThat(converter.convert("{\"field1\":\"value:1\",\"field2\":\"value: 2\"}"), is("<object><field1>value:1</field1><field2>value: 2</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithTwoFieldsAndWhitespaceBetweenPairs() {
        assertThat(converter.convert("{\"field1\":\"value1\" \n   , \n \t  \"field2\":false}"), is("<object><field1>value1</field1><field2>false</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelSimpleObjectWithWhitespaceBetweenColons() {
        assertThat(converter.convert("{\"field1\" : \"value:1\" , \"field2\" \n : \t \"value: 2\"}"), is("<object><field1>value:1</field1><field2>value: 2</field2></object>"));
    }


    // ========================================================================
    // Tests nested JSON
    // ========================================================================

    
}
