package com.xebia.workshops;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class InputParserTest {


    @Test
    public void inputParserShouldParseTitles() throws URISyntaxException {
        File input = new File(getClass().getResource("/input1.json").toURI());

        List<String> titles = new InputParser().parseTitles(input);

        assertThat(titles, notNullValue());
        assertThat(titles.size(), is(10));
        assertThat(titles.get(0), is("Some quick experiments with Web sockets using Atmosphere"));
        assertThat(titles.get(9), is("Project Pitch: LogStore"));
    }

    @Test
    public void inputParserShouldParseJsonIds() throws URISyntaxException {
        File input = new File(getClass().getResource("/input1.json").toURI());

        List<Long> ids = new InputParser().parseIds(input);

        assertThat(ids, notNullValue());
        assertThat(ids.size(), is(35));
        assertThat(ids.get(0), is(0L));
        assertThat(ids.get(9), is(1L));
    }
}
