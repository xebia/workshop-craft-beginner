package com.xebia.workshops;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author iwein
 */
public class LockingLineMultiplexer implements LineMultiplexer {

    private final Map<String, Queue<String>> queueMap = new HashMap<String, Queue<String>>();


    @Override
    public void addLineForKey(String key, String line) {
        getQueueForKey(key).offer(line);
    }

    private Queue<String> getQueueForKey(String key) {
        if(!queueMap.containsKey(key)){
            queueMap.put(key, new LinkedList<String>());
        }
        return queueMap.get(key);
    }

    @Override
    public String nextLineFor(String key) {
        return getQueueForKey(key).poll();
    }
}
