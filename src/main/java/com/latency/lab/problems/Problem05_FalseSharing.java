package com.latency.lab.problems;

import com.latency.lab.LatencyHistogram;

/**
 * Problem 5: False Sharing and Cache Line Contention
 * 
 * Multiple threads modifying variables on the same cache line causes
 * performance degradation.
 */
public class Problem05_FalseSharing {

    static class ProblematicData {
        public long counter1 = 0;  // Cache line 1
        public long counter2 = 0;  // Same cache line (false sharing)
    }

    static class OptimizedData {
        public long counter1 = 0;  // Cache line 1
        // Padding to 64 bytes (typical cache line size)
        public long p2, p3, p4, p5, p6, p7, p8;
        public long counter2 = 0;  // Different cache line
        public long p10, p11, p12, p13, p14, p15, p16;
    }

    static class FalseSharingTest implements Runnable {
        private final LatencyHistogram histogram;
        private final Object data;
        private final int counterIndex;
        private final int iterations;
        private final boolean isProblematic;

        public FalseSharingTest(LatencyHistogram histogram, Object data,
                               int counterIndex, int iterations, boolean isProblematic) {
            this.histogram = histogram;
            this.data = data;
            this.counterIndex = counterIndex;
            this.iterations = iterations;
            this.isProblematic = isProblematic;
        }

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                if (isProblematic) {
                    ProblematicData d = (ProblematicData) data;
                    if (counterIndex == 0) {
                        d.counter1++;
                    } else {
                        d.counter2++;
                    }
                } else {
                    OptimizedData d = (OptimizedData) data;
                    if (counterIndex == 0) {
                        d.counter1++;
                    } else {
                        d.counter2++;
                    }
                }
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n" + "#".repeat(60));
        System.out.println("# Problem 05: False Sharing and Cache Line Contention");
        System.out.println("#".repeat(60));

        int iterations = 50000;

        // Test problematic version
        System.out.println("\n[TEST 1] Problematic: Same Cache Line (False Sharing)");
        LatencyHistogram problematicHist = new LatencyHistogram();
        ProblematicData problematicData = new ProblematicData();
        Thread[] threads1 = new Thread[2];
        
        for (int i = 0; i < 2; i++) {
            threads1[i] = new Thread(new FalseSharingTest(problematicHist, problematicData,
                                                          i, iterations, true));
            threads1[i].start();
        }
        
        for (Thread t : threads1) {
            t.join();
        }
        problematicHist.printSummary("False Sharing - Same Cache Line");

        // Test optimized version
        System.out.println("\n[TEST 2] Optimized: Padded Cache Lines");
        LatencyHistogram optimizedHist = new LatencyHistogram();
        OptimizedData optimizedData = new OptimizedData();
        Thread[] threads2 = new Thread[2];
        
        for (int i = 0; i < 2; i++) {
            threads2[i] = new Thread(new FalseSharingTest(optimizedHist, optimizedData,
                                                          i, iterations, false));
            threads2[i].start();
        }
        
        for (Thread t : threads2) {
            t.join();
        }
        optimizedHist.printSummary("False Sharing - Padded Cache Lines");
    }
