package com.xebia.workshops;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author iwein
 */
public class LockingLineMultiplexer implements LineMultiplexer {

    private final Queue<String> work = new LinkedList<String>();

    private final Object writeLock = new Object();

    private final Object readLock = new Object();


    @Override
    public void addLine(String line) {
        synchronized (readLock) {
            synchronized (writeLock) {
                work.offer(line);
            }
        }
    }

    @Override
    public String nextLine() {
        synchronized (writeLock) {
            synchronized (readLock) {
                return work.poll();
            }
        }
    }
}
