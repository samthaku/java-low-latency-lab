package com.latency.lab.problems;

import com.latency.lab.LatencyHistogram;
import com.latency.lab.GCMonitor;

/**
 * Problem 1: Garbage Collection Pauses
 * 
 * The JVM's garbage collector can pause application threads, introducing
 * unpredictable latency spikes ("stop-the-world" events).
 */
public class Problem01_GCPauses {

    static class ProblematicVersion {
        public void processWithExcessiveAllocation(int iterations) {
            LatencyHistogram histogram = new LatencyHistogram();
            GCMonitor gc = new GCMonitor();
            gc.start();

            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                // Creating many short-lived objects causing GC pressure
                for (int j = 0; j < 1000; j++) {
                    byte[] trash = new byte[1024]; // Allocate 1MB per iteration
                }
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
            
            histogram.printSummary("GC Pauses - Problematic Version");
            gc.report();
        }
    }

    static class OptimizedVersion {
        private static final int POOL_SIZE = 1000;
        private final byte[][] pool = new byte[POOL_SIZE][];

        public OptimizedVersion() {
            // Pre-allocate off-heap buffer
            for (int i = 0; i < POOL_SIZE; i++) {
                pool[i] = new byte[1024];
            }
        }

        public void processWithObjectPooling(int iterations) {
            LatencyHistogram histogram = new LatencyHistogram();
            GCMonitor gc = new GCMonitor();
            gc.start();

            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                // Reuse objects from pool - no new allocations
                int poolIndex = i % POOL_SIZE;
                byte[] buffer = pool[poolIndex];
                // Use buffer...
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
            
            histogram.printSummary("GC Pauses - Optimized Version (Object Pooling)");
            gc.report();
        }
    }

    public static void main(String[] args) {
        System.out.println("\n" + "#".repeat(60));
        System.out.println("# Problem 01: Garbage Collection Pauses");
        System.out.println("#".repeat(60));

        int iterations = 10000;

        System.out.println("\n[TEST 1] Problematic: Excessive Object Allocation");
        ProblematicVersion problematic = new ProblematicVersion();
        problematic.processWithExcessiveAllocation(iterations);

        // Wait for GC
        System.gc();
        Thread.yield();

        System.out.println("\n[TEST 2] Optimized: Object Pooling");
        OptimizedVersion optimized = new OptimizedVersion();
        optimized.processWithObjectPooling(iterations);
    }
}