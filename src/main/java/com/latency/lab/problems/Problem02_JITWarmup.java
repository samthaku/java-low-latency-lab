package com.latency.lab.problems;

import com.latency.lab.LatencyHistogram;

/**
 * Problem 2: JIT Warm-up Delays
 * 
 * The Just-In-Time (JIT) compiler optimizes code at runtime, which can cause
 * latency during startup (warm-up phase).
 */
public class Problem02_JITWarmup {

    static class Calculator {
        public long fibonacci(int n) {
            if (n <= 1) return n;
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n" + "#".repeat(60));
        System.out.println("# Problem 02: JIT Warm-up Delays");
        System.out.println("#".repeat(60));

        Calculator calc = new Calculator();
        LatencyHistogram histogram = new LatencyHistogram();

        int warmupIterations = 50000;  // JIT warm-up
        int testIterations = 10000;    // Actual test

        // Warm-up phase (JIT compilation happening)
        System.out.println("\n[WARM-UP] Running " + warmupIterations + " iterations (JIT compilation)...");
        for (int i = 0; i < warmupIterations; i++) {
            long start = System.nanoTime();
            calc.fibonacci(20);
            long latency = System.nanoTime() - start;
            if (i >= warmupIterations - 100) {
                histogram.recordLatency(latency);
            }
        }
        System.out.println("Warm-up complete. JIT should be optimized now.");

        // Test phase (after JIT compilation)
        System.out.println("\n[TEST] Measuring latency after JIT warm-up...");
        LatencyHistogram testHistogram = new LatencyHistogram();
        for (int i = 0; i < testIterations; i++) {
            long start = System.nanoTime();
            calc.fibonacci(20);
            long latency = System.nanoTime() - start;
            testHistogram.recordLatency(latency);
        }

        histogram.printSummary("JIT Warm-up - Late Warm-up Phase");
        testHistogram.printSummary("JIT Warm-up - After Optimization");

        System.out.println("\n[INSIGHT] Notice the latency difference between warm-up and optimized phases.");
        System.out.println("Early measurements show higher variability due to JIT compilation.");
    }
}