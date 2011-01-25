package com.xebia.workshops;

/**
 * The Line multiplexer can hold strings under a certain key and reproduce these strings per key in the order that they
 * were added to the multiplexer.
 *
 * @author iwein
 */
public interface LineMultiplexer {
    /**
     * Queue the given line
     */
    public void addLine(String line);

    /**
     * @return the line
     */
    String nextLine();
}
