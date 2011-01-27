package com.xebia.workshops;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonToXmlConverterTest {
    private JsonToXmlConverter converter = new JsonToXmlConverter();

    // ========================================================================
    // Tests for top-level arrays and basic data types
    // ========================================================================

    @Test
    public void shouldConvertTopLevelEmptyArray() {
        assertThat(converter.convert("[]"), is("<array></array>"));
    }

    @Test
    public void shouldConvertTopLevelEmptyArrayContainingWhitespace() {
        assertThat(converter.convert("[ ]"), is("<array></array>"));
        assertThat(converter.convert("[ \n\t  ]"), is("<array></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleInteger() {
        assertThat(converter.convert("[1]"), is("<array><item>1</item></array>"));
        assertThat(converter.convert("[42]"), is("<array><item>42</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleFloat() {
        assertThat(converter.convert("[0.3]"), is("<array><item>0.3</item></array>"));
        assertThat(converter.convert("[111.333]"), is("<array><item>111.333</item></array>"));
        assertThat(converter.convert("[3.14159265358979323846264338327950288]"), is("<array><item>3.14159265358979323846264338327950288</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfNumbers() {
        assertThat(converter.convert("[1,0.2,3.14159265358979323846264338327950288]"),
            is("<array><item>1</item><item>0.2</item><item>3.14159265358979323846264338327950288</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithWhitespaceBetweenNumbers() {
        assertThat(converter.convert("[1 , 2, \n 3]"), is("<array><item>1</item><item>2</item><item>3</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleBoolean() {
        assertThat(converter.convert("[false]"), is("<array><item>false</item></array>"));
    }

    @Test(expected=IllegalStateException.class)
    public void shouldFailOnTopLevelArrayOfSingleInvalidBooleanFalse() {
        assertThat(converter.convert("[falce]"), is("<array><item>false</item></array>"));
    }

    @Test(expected=IllegalStateException.class)
    public void shouldFailOnTopLevelArrayOfSingleInvalidBooleanTrue() {
        assertThat(converter.convert("[trui]"), is("<array><item>true</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfBooleans() {
        assertThat(converter.convert("[true,false,true]"), is("<array><item>true</item><item>false</item><item>true</item></array>"));
        assertThat(converter.convert("[false,true,false]"), is("<array><item>false</item><item>true</item><item>false</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithWhitespaceBetweenBooleans() {
        assertThat(converter.convert("[true \n, false  , \t  true]"), is("<array><item>true</item><item>false</item><item>true</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleString() {
        assertThat(converter.convert("[\"string\"]"), is("<array><item>string</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleStringContainingWhitespace() {
        assertThat(converter.convert("[\"strings are funny things\"]"), is("<array><item>strings are funny things</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleStringWithEscapeSequences() {
        assertThat(converter.convert("[\"str\\ni\\\\ng\\\\\"]"), is("<array><item>str\\ni\\\\ng\\\\</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleStringWithEscapedQuote() {
        assertThat(converter.convert("[\"str\\\"ing\"]"), is("<array><item>str\\\"ing</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfStrings() {
        assertThat(converter.convert("[\"string1\" , \"string2\", \"string3\"]"),
            is("<array><item>string1</item><item>string2</item><item>string3</item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayOfSingleNull() {
        assertThat(converter.convert("[null]"), is("<array><item></item></array>"));
    }


    // ========================================================================
    // Tests for simple, non-nested JSON objects
    // ========================================================================

    @Test
    public void shouldConvertTopLevelEmptyObject() {
        assertThat(converter.convert("{}"), is("<object></object>"));
    }

    @Test
    public void shouldConvertTopLevelEmptyObjectContainingWhitespace() {
        assertThat(converter.convert("{ }"), is("<object></object>"));
        assertThat(converter.convert("{ \n\t\t }"), is("<object></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleStringField() {
        assertThat(converter.convert("{\"field1\":\"value1\"}"), is("<object><field1>value1</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleIntegerField() {
        assertThat(converter.convert("{\"field1\":42}"), is("<object><field1>42</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleFloatField() {
        assertThat(converter.convert("{\"field1\":42.0}"), is("<object><field1>42.0</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleTrueField() {
        assertThat(converter.convert("{\"field1\":true}"), is("<object><field1>true</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleFalseField() {
        assertThat(converter.convert("{\"field1\":false}"), is("<object><field1>false</field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithSingleNullField() {
        assertThat(converter.convert("{\"field1\":null}"), is("<object><field1></field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithTwoStringFields() {
        assertThat(converter.convert("{\"field1\":\"value1\",\"field2\":\"value2\"}"),
            is("<object><field1>value1</field1><field2>value2</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithMixedFields() {
        assertThat(converter.convert("{\"field1\":\"value1\",\"field2\":42,\"field3\":false,\"field4\":42.0}"),
            is("<object>" +
               "<field1>value1</field1>" +
               "<field2>42</field2>" +
               "<field3>false</field3>" +
               "<field4>42.0</field4>" +
               "</object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithFieldNamesContainingColons() {
        assertThat(converter.convert("{\"field:1\":\"value:1\",\"field:2\":\"value: 2\"}"),
            is("<object><field:1>value:1</field:1><field:2>value: 2</field:2></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithFieldValuesContainingColons() {
        assertThat(converter.convert("{\"field1\":\"value:1\",\"field2\":\"value: 2\"}"),
            is("<object><field1>value:1</field1><field2>value: 2</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithTwoFieldsAndWhitespaceBetweenPairs() {
        assertThat(converter.convert("{\"field1\":\"value1\" \n   , \n \t  \"field2\":false}"),
            is("<object><field1>value1</field1><field2>false</field2></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithWhitespaceBetweenColons() {
        assertThat(converter.convert("{\"field1\" : \"value:1\" , \"field2\" \n : \t \"value: 2\"}"),
            is("<object><field1>value:1</field1><field2>value: 2</field2></object>"));
    }


    // ========================================================================
    // Tests for arrays containing other arrays and objects
    // ========================================================================

    @Test
    public void shouldConvertTopLevelArrayWithNestedEmptyArray() {
        assertThat(converter.convert("[[]]"),
            is("<array><item><array></array></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithNestedEmptyArrays() {
        assertThat(converter.convert("[[] ,[  \t], [\n]]"),
            is("<array><item><array></array></item><item><array></array></item><item><array></array></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithNestedArray() {
        assertThat(converter.convert("[[1,2,3], \t []]"),
            is("<array>" +
               "<item><array><item>1</item><item>2</item><item>3</item></array></item>" +
               "<item><array></array></item>" +
               "</array>"));
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

    @Test
    public void shouldConvertTopLevelArrayWithNestedEmptyObject() {
        assertThat(converter.convert("[{}]"), is("<array><item><object></object></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithNestedEmptyObjects() {
        assertThat(converter.convert("[{},\n {} , {}]"),
            is("<array><item><object></object></item><item><object></object></item><item><object></object></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithSingleNestedObject() {
        assertThat(converter.convert("[{\"field1\":\"value1\"}]"),
            is("<array><item><object><field1>value1</field1></object></item></array>"));
    }

    @Test
    public void shouldConvertTopLevelArrayWithNestedEmptyObjectsAndArrays() {
        assertThat(converter.convert("[{}, [] , {}]"),
            is("<array><item><object></object></item><item><array></array></item><item><object></object></item></array>"));
    }


    // ========================================================================
    // Tests for objects containing other objects and arrays
    // ========================================================================

    @Test
    public void shouldConvertTopLevelObjectWithEmptyArrayAsFieldValue() {
        assertThat(converter.convert("{\"field1\":[]}"),
            is("<object><field1><array></array></field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithArrayAsFieldValue() {
        assertThat(converter.convert("{\"field1\":[1, 2 , 3], \"field2\":[\"1\" \n, \"2\" , \"3\"]}"),
            is("<object>" +
               "<field1><array><item>1</item><item>2</item><item>3</item></array></field1>" +
               "<field2><array><item>1</item><item>2</item><item>3</item></array></field2>" +
               "</object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithEmptyObjectAsFieldValue() {
        assertThat(converter.convert("{\"field1\":{}}"),
            is("<object><field1><object></object></field1></object>"));
    }

    @Test
    public void shouldConvertTopLevelObjectWithObjectAsFieldValue() {
        assertThat(converter.convert("{\"field1\":{\"field2\":\"value1\"}}"),
            is("<object><field1><object><field2>value1</field2></object></field1></object>"));
    }


    // ========================================================================
    // Kitchen sink tests: if this doesn't break it...
    // ========================================================================

    @Test
    public void shouldConvertSampleTwitterOutput() {
        // some JSON straight from Twitter with some nice Chinese text. Lots of fields removed for brevity
        assertThat(
            converter.convert("[\n" +
                "  {\n" +
                "    \"coordinates\": null,\n" +
                "    \"favorited\": false,\n" +
                "    \"created_at\": \"Thu Jan 20 15:22:50 +0000 2011\",\n" +
                "    \"id_str\": \"28110206533632000\",\n" +
                "    \"id\": 28110206533632000,\n" +
                "    \"text\": \"\\u201c\\u5728\\u8fd9\\u4e2a\\u56fd\\u5bb6\\uff0c\\u65f6\\u95f4\\u662f\\u505c\\u6b62\\u4f4f\\u7684\\u3002\\u6240\\u6709\\u4e00\\u5207\\u90fd\\u88ab\\u51bb\\u7ed3\\u5728\\u4e8650\\u5e74\\u524d\\u3002\\u201d\\u5eb7\\u5fb7\\u65af\\u8bf4http:\\/\\/goo.gl\\/Gw8FZ  \\u300aLIFE\\u300b\\u6742\\u5fd7\\u7279\\u9080\\u8bf7\\u4e86\\u4e94\\u4f4d\\u83b7\\u5956\\u6444\\u5f71\\u5e08\\uff0c\\u6765\\u4e0e\\u8bfb\\u8005\\u5206\\u4eab\\u4ed6\\u4eec\\u5728\\u5317\\u671d\\u9c9c\\u5b9a\\u683c\\u4e0b\\u7684\\u5f71\\u50cf\\uff0c\\u4ee5\\u53ca\\u4ed6\\u4eec\\u6240\\u7ecf\\u5386\\u8fc7\\u7684\\u771f\\u5b9e\",\n" +
                "    \"in_reply_to_status_id_str\": null,\n" +
                "    \"user\": {\n" +
                "      \"name\": \"\\u6c6a\\u82e5\\u6d77\",\n" +
                "      \"profile_image_url\": \"http:\\/\\/a2.twimg.com\\/profile_images\\/587306890\\/my_shoes_ps_normal.jpg\",\n" +
                "      \"id\": 15818674,\n" +
                "      \"description\": \"\\u5b85\\u7537\\uff0c\\u6df1\\u5733\\u6253\\u5de5\\u4e2d\\u2026\\u2026\",\n" +
                "      \"profile_background_image_url\": \"http:\\/\\/a3.twimg.com\\/a\\/1295376387\\/images\\/themes\\/theme1\\/bg.png\",\n" +
                "      \"screen_name\": \"Ruohai\"\n" +
                "    },\n" +
                "    \"place\": null,\n" +
                "    \"source\": \"\\u003Ca href=\\\"https:\\/\\/tuite.im\\\" rel=\\\"nofollow\\\"\\u003E\\u654f\\u611f\\u8bcd\\u003C\\/a\\u003E\",\n" +
                "    \"in_reply_to_status_id\": null\n" +
                "  }]"),
            is(
                "<array>" +
                    "<item>" +
                    "<object>" +
                        "<coordinates></coordinates>" +
                        "<favorited>false</favorited>" +
                        "<created_at>Thu Jan 20 15:22:50 +0000 2011</created_at>" +
                        "<id_str>28110206533632000</id_str>" +
                        "<id>28110206533632000</id>" +
                        "<text>\\u201c\\u5728\\u8fd9\\u4e2a\\u56fd\\u5bb6\\uff0c\\u65f6\\u95f4\\u662f\\u505c\\u6b62\\u4f4f\\u7684\\u3002\\u6240\\u6709\\u4e00\\u5207\\u90fd\\u88ab\\u51bb\\u7ed3\\u5728\\u4e8650\\u5e74\\u524d\\u3002\\u201d\\u5eb7\\u5fb7\\u65af\\u8bf4http:\\/\\/goo.gl\\/Gw8FZ  \\u300aLIFE\\u300b\\u6742\\u5fd7\\u7279\\u9080\\u8bf7\\u4e86\\u4e94\\u4f4d\\u83b7\\u5956\\u6444\\u5f71\\u5e08\\uff0c\\u6765\\u4e0e\\u8bfb\\u8005\\u5206\\u4eab\\u4ed6\\u4eec\\u5728\\u5317\\u671d\\u9c9c\\u5b9a\\u683c\\u4e0b\\u7684\\u5f71\\u50cf\\uff0c\\u4ee5\\u53ca\\u4ed6\\u4eec\\u6240\\u7ecf\\u5386\\u8fc7\\u7684\\u771f\\u5b9e</text>" +
                        "<in_reply_to_status_id_str></in_reply_to_status_id_str>" +
                        "<user>" +
                            "<object>" +
                                "<name>\\u6c6a\\u82e5\\u6d77</name>" +
                                "<profile_image_url>http:\\/\\/a2.twimg.com\\/profile_images\\/587306890\\/my_shoes_ps_normal.jpg</profile_image_url>" +
                                "<id>15818674</id>" +
                                "<description>\\u5b85\\u7537\\uff0c\\u6df1\\u5733\\u6253\\u5de5\\u4e2d\\u2026\\u2026</description>" +
                                "<profile_background_image_url>http:\\/\\/a3.twimg.com\\/a\\/1295376387\\/images\\/themes\\/theme1\\/bg.png</profile_background_image_url>" +
                                "<screen_name>Ruohai</screen_name>" +
                            "</object>" +
                        "</user>" +
                        "<place></place>" +
                        "<source>\\u003Ca href=\\\"https:\\/\\/tuite.im\\\" rel=\\\"nofollow\\\"\\u003E\\u654f\\u611f\\u8bcd\\u003C\\/a\\u003E</source>" +
                        "<in_reply_to_status_id></in_reply_to_status_id>" +
                    "</object>" +
                    "</item>" +
                "</array>"));
    }


}
