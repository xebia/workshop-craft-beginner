package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @author iwein
 */
public class LockingLineMultiplexer implements LineMultiplexer{

    private final Map<String, Queue<String>> queueMap = new HashMap<String, Queue<String>>();


    @Override
    public void addLineForKey(String line, String key) {
        queueMap.get(key).offer(line);
    }

    @Override
    public String nextLineFor(String key) {
        queueMap.get(key).poll();
    }
}
