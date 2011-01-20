package com.xebia.workshops;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author iwein
 */
public class LockingLineMultiplexerTest {

    private final LockingLineMultiplexer multiplexer = new LockingLineMultiplexer();

    @Test
    public void shouldReturnLineAddedForKey() throws Exception {
        multiplexer.addLineForKey("This is the line", "Key");
        assertThat(multiplexer.nextLineFor("Key"), is("This is the line"));
    }

    @Test
    public void shouldReturnLinesInRightOrder() throws Exception {
        String line1 = "This is the first line";
        String key1 = "Key";
        String line2 = "This is the first line";
        String key2 = "Key";
        multiplexer.addLineForKey(line1, key1);
        multiplexer.addLineForKey(line2, key2);
        assertThat(multiplexer.nextLineFor(key1), is(line1));
        assertThat(multiplexer.nextLineFor(key2), is(line2));
    }
}
