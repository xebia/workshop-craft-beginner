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
    public void shouldConvertTopLevelEmptyArray() {
        assertThat(converter.convert("[]"), is("<array></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleNumber() {
        assertThat(converter.convert("[1]"), is("<array><item>1</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleBoolean() {
        assertThat(converter.convert("[false]"), is("<array><item>false</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfNumbers() {
        assertThat(converter.convert("[1,2,3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithWhitespaceBetweenNumbers() {
        assertThat(converter.convert("[1 , 2, \n 3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfBooleans() {
        assertThat(converter.convert("[true,false,true]"), is("<array><item>true</item><item>false</item><item>true</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithWhitespaceBetweenBoolean() {
        assertThat(converter.convert("[true \n, false  , \t  true]"), is("<array><item>true</item><item>false</item><item>true</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelEmptyObject() {
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

    @Test
    public void shouldConvertTopLevelArrayWithSingleNestedArray() {
        assertThat(converter.convert("[[1]]"), is("<array><item><array><item>1</item></array></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithNestedArrays() {
        assertThat(converter.convert("[1,[2,3, 4],[5 ,6,\n7]]"),
            is("<array>" +
                   "<item>1</item>" +
                   "<item><array><item>2</item><item>3</item><item>4</item></array></item>" +
                   "<item><array><item>5</item><item>6</item><item>7</item></array></item>" +
               "</array>"));
    }
    
}
