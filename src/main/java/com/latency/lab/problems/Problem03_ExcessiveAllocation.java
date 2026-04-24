package com.latency.lab.problems;

import com.latency.lab.LatencyHistogram;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem 3: Excessive Object Allocation
 * 
 * Creating too many short-lived objects increases GC pressure.
 */
public class Problem03_ExcessiveAllocation {

    static class ProblematicVersion {
        public void processWithBoxing(int iterations) {
            LatencyHistogram histogram = new LatencyHistogram();

            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                List<Integer> numbers = new ArrayList<>();
                for (int j = 0; j < 100; j++) {
                    numbers.add(j);  // Autoboxing: int -> Integer (allocation)
                }
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
            
            histogram.printSummary("Excessive Allocation - Autoboxing Version");
        }
    }

    static class OptimizedVersion {
        public void processWithPrimitives(int iterations) {
            LatencyHistogram histogram = new LatencyHistogram();

            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                
                int[] numbers = new int[100];  // No allocations in loop
                for (int j = 0; j < 100; j++) {
                    numbers[j] = j;  // Primitive assignment
                }
                
                long latency = System.nanoTime() - start;
                histogram.recordLatency(latency);
            }
            
            histogram.printSummary("Excessive Allocation - Primitive Array Version");
        }
    }

    public static void main(String[] args) {
        System.out.println("\n" + "#".repeat(60));
        System.out.println("# Problem 03: Excessive Object Allocation");
        System.out.println("#".repeat(60));

        int iterations = 5000;

        System.out.println("\n[TEST 1] Problematic: Autoboxing in List");
        ProblematicVersion problematic = new ProblematicVersion();
        problematic.processWithBoxing(iterations);

        System.gc();
        Thread.yield();

        System.out.println("\n[TEST 2] Optimized: Primitive Arrays");
        OptimizedVersion optimized = new OptimizedVersion();
        optimized.processWithPrimitives(iterations);
    }
}