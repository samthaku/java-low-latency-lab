package com.latency.lab;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Monitor for tracking GC activity and its impact on latency.
 */
public class GCMonitor {
    private long startGCCount;
    private long startGCTime;

    public void start() {
        startGCCount = getTotalGCCount();
        startGCTime = getTotalGCTime();
    }

    public void report() {
        long endGCCount = getTotalGCCount();
        long endGCTime = getTotalGCTime();
        
        long gcCount = endGCCount - startGCCount;
        long gcTime = endGCTime - startGCTime;
        
        System.out.println("\nGC Activity:");
        System.out.printf("  GC Collections: %d%n", gcCount);
        System.out.printf("  GC Time:        %d ms%n", gcTime);
    }

    private long getTotalGCCount() {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        return beans.stream().mapToLong(GarbageCollectorMXBean::getCollectionCount).sum();
    }

    private long getTotalGCTime() {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        return beans.stream().mapToLong(GarbageCollectorMXBean::getCollectionTime).sum();
    }
}