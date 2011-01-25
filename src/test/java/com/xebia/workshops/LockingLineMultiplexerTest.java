package com.xebia.workshops;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.hasItem;
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
        multiplexer.addLine("This is the line");
        assertThat(multiplexer.nextLine(), is("This is the line"));
    }

    @Test
    public void shouldReturnLinesInRightOrder() throws Exception {
        String line1 = "This is the first line";
        String line2 = "This is the second line";
        multiplexer.addLine(line1);
        multiplexer.addLine(line2);
        assertThat(multiplexer.nextLine(), is(line1));
        assertThat(multiplexer.nextLine(), is(line2));
    }

    /* From here tests are concurrent */
    @Test (timeout = 2000)
    @Ignore //this test will fail in a deadlock
    public void shouldSurviveConcurrentAccess() throws Exception {
        CountDownLatch startTest = new CountDownLatch(1);

        ArrayList<CountDownLatch> sentLatches = new ArrayList<CountDownLatch>();
        int numberOfLinesAdded = 500;
        for (int i = 0; i < numberOfLinesAdded; i++) {
            sentLatches.add(addConcurrently(startTest, "line " + i));
        }
        final List<String> results = new CopyOnWriteArrayList<String>();
        CountDownLatch doneReading = readConcurrently(startTest, results, numberOfLinesAdded);
        startTest.countDown();
        for (CountDownLatch sentLatch : sentLatches) {
            sentLatch.await();
        }
        doneReading.await();
        for (int i = 0; i < numberOfLinesAdded; i++) {
            assertThat(results, hasItem("line " + i));
        }
        System.out.println(results);
    }

    private CountDownLatch addConcurrently(final CountDownLatch startTest,  String... lines) {
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
                    multiplexer.addLine(line);
                    allSent.countDown();
                }
            });
        }
        return allSent;
    }

    private CountDownLatch readConcurrently(final CountDownLatch startTest, final List<String> results, final int numberOfReads) {
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
                    String nextLine = multiplexer.nextLine();
                    while (nextLine == null) {
                        nextLine = multiplexer.nextLine();
                    }
                    System.out.println("Read: "+nextLine);
                    results.add(nextLine);
                    doneReading.countDown();
                }
            });
        }
        return doneReading;
    }
}
