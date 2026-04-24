package com.latency.lab.problems;

import com.latency.lab.LatencyHistogram;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Problem 4: Synchronization and Lock Contention
 * 
 * Excessive lock contention and synchronized blocks cause thread
 * contention and waiting.
 */
public class Problem04_SynchronizationContention {

    static class ProblematicCounter {
        private long value = 0;

        public synchronized void increment() {
            value++;
        }

        public synchronized long getValue() {
            return value;
        }
    }

    static class OptimizedCounter {
        private final AtomicLong value = new AtomicLong(0);

        public void increment() {
            value.incrementAndGet();
        }

        public long getValue() {
            return value.get();
        }
    }

    static class ContentionTest implements Runnable {
        private final LatencyHistogram histogram;
        private final Object counter;
        private final int iterations;
        private final boolean isProblematic;

        public ContentionTest(LatencyHistogram histogram, Object counter, 
                             int iterations, boolean isProblematic) {
            this.histogram = histogram;
            this.counter = counter;
            this.iterations = iterations;
            this.isProblematic = isProblematic;
        }

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                if (isProblematic) {
                    ((ProblematicCounter) counter).increment();
                } else {
                    ((OptimizedCounter) counter).increment();
                }
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n" + "#".repeat(60));
        System.out.println("# Problem 04: Synchronization and Lock Contention");
        System.out.println("#".repeat(60));

        int threadCount = 8;
        int iterationsPerThread = 5000;

        // Test problematic version
        System.out.println("\n[TEST 1] Problematic: Synchronized Method");
        LatencyHistogram problematicHist = new LatencyHistogram();
        ProblematicCounter problematicCounter = new ProblematicCounter();
        Thread[] threads1 = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads1[i] = new Thread(new ContentionTest(problematicHist, problematicCounter, 
                                                        iterationsPerThread, true));
            threads1[i].start();
        }
        
        for (Thread t : threads1) {
            t.join();
        }
        problematicHist.printSummary("Lock Contention - Synchronized Version");

        // Test optimized version
        System.out.println("\n[TEST 2] Optimized: AtomicLong");
        LatencyHistogram optimizedHist = new LatencyHistogram();
        OptimizedCounter optimizedCounter = new OptimizedCounter();
        Thread[] threads2 = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads2[i] = new Thread(new ContentionTest(optimizedHist, optimizedCounter, 
                                                        iterationsPerThread, false));
            threads2[i].start();
        }
        
        for (Thread t : threads2) {
            t.join();
        }
        optimizedHist.printSummary("Lock Contention - AtomicLong Version");
    }
}