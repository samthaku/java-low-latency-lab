package com.latency.lab;

import org.HdrHistogram.Histogram;

/**
 * Utility class for recording and analyzing latency measurements.
 * Provides percentile analysis using HdrHistogram.
 */
public class LatencyHistogram {
    private final Histogram histogram;

    public LatencyHistogram() {
        // Records latency in nanoseconds with 3 decimal places precision
        this.histogram = new Histogram(3600000000000L, 3);
    }

    public void recordLatency(long nanos) {
        histogram.recordValue(nanos);
    }

    public void printSummary(String problemName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Problem: " + problemName);
        System.out.println("=".repeat(60));
        System.out.printf("Total Operations: %,d%n", histogram.getTotalCount());
        System.out.printf("Mean:             %.2f ns (%.3f μs)%n", 
            histogram.getMean(), histogram.getMean() / 1000.0);
        System.out.printf("Median (p50):     %.0f ns (%.3f μs)%n", 
            histogram.getValueAtPercentile(50), histogram.getValueAtPercentile(50) / 1000.0);
        System.out.printf("p90:              %.0f ns (%.3f μs)%n", 
            histogram.getValueAtPercentile(90), histogram.getValueAtPercentile(90) / 1000.0);
        System.out.printf("p99:              %.0f ns (%.3f μs)%n", 
            histogram.getValueAtPercentile(99), histogram.getValueAtPercentile(99) / 1000.0);
        System.out.printf("p99.9:            %.0f ns (%.3f μs)%n", 
            histogram.getValueAtPercentile(99.9), histogram.getValueAtPercentile(99.9) / 1000.0);
        System.out.printf("p99.99:           %.0f ns (%.3f μs)%n", 
            histogram.getValueAtPercentile(99.99), histogram.getValueAtPercentile(99.99) / 1000.0);
        System.out.printf("Max:              %.0f ns (%.3f μs)%n", 
            (double) histogram.getMaxValue(), histogram.getMaxValue() / 1000.0);
        System.out.println("=".repeat(60));
    }

    public long getPercentile(double percentile) {
        return histogram.getValueAtPercentile(percentile);
    }

    public long getMeanLatency() {
        return Math.round(histogram.getMean());
    }
}