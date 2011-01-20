package com.xebia.workshops;

/**
 * The Line multiplexer can hold strings under a certain key and reproduce these strings per key in the order that they
 * were added to the multiplexer.
 *
 * @author iwein
 */
public interface LineMultiplexer {
    /**
     * Queue the given line for the given key
     */
    void addLineForKey(String line ,String key);

    /**
     * @return the line for the given key
     */
    String nextLineFor(String key);
}
