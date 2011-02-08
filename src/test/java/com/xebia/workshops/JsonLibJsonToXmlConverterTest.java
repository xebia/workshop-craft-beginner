package com.xebia.workshops;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertThat;

/**
 * @author iwein
 */
public class JsonLibJsonToXmlConverterTest {

    @Test
    public void shouldConvertEmptyArrayToXml() throws Exception {
        String converted = new JsonLibJsonToXmlConverter("[]").asXml();
        assertThat(converted,
                anyOf(
                        containsString("<array></array>"),
                        containsString("<array/>")));
    }

    @Test
    public void shouldConvertNumberArrayToXml() throws Exception {
        String converted = new JsonLibJsonToXmlConverter("[1,2,3]").asXml();
        assertThat(converted, containsString("<array><item>1</item><item>2</item><item>3</item></array>"));
    }
}
