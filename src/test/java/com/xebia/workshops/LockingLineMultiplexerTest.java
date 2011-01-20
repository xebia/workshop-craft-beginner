package com.xebia.workshops;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author iwein
 */
public class LockingLineMultiplexerTest {

    private final LockingLineMultiplexer multiplexer = new LockingLineMultiplexer();

    ExecutorService executor = Executors.newFixedThreadPool(10);

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

    /* From here tests are concurrent */
    @Test
    public void shouldSurviveConcurrentAccess() throws Exception {
        CountDownLatch startTest = new CountDownLatch(1);

        CountDownLatch allSent = addConcurrently(startTest, "key", "one", "two", "three", "four", "five", "six");
        final List<String> results = new CopyOnWriteArrayList<String>();
        CountDownLatch doneReading = readConcurrently(startTest, results, "key", 6);
        startTest.countDown();
        allSent.await();
        doneReading.await();
        System.out.println(results);
    }

    private CountDownLatch addConcurrently(final CountDownLatch startTest, final String key, String... lines) {
        final CountDownLatch allSent = new CountDownLatch(lines.length);
        for (final String line : lines) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        startTest.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    multiplexer.addLineForKey(key, line);
                    allSent.countDown();
                }
            });
        }
        return allSent;
    }

    private CountDownLatch readConcurrently(final CountDownLatch startTest, final List<String> results, final String key, final int numberOfReads) {
        final CountDownLatch doneReading = new CountDownLatch(numberOfReads);
        for (int i = 0; i < numberOfReads; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        startTest.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    String nextLine = multiplexer.nextLineFor(key);
                    while (nextLine == null) {
                        nextLine = multiplexer.nextLineFor(key);
                    }
                    System.out.println("next line: "+nextLine);
                    results.add(nextLine);
                    doneReading.countDown();
                }
            });
        }
        return doneReading;
    }
}
