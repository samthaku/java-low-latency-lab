# Java Low Latency Lab

A comprehensive laboratory to test and analyze the **top 20 Java low-latency performance problems**.

## Overview

This project provides detailed demonstrations of common latency issues in Java applications, with both **problematic** and **optimized** implementations to compare performance characteristics.

## Problems Covered

### Implemented
1. **Garbage Collection Pauses** - Object pooling vs excessive allocation
2. **JIT Warm-up Delays** - Compilation overhead and optimization
3. **Excessive Object Allocation** - Autoboxing vs primitives
4. **Synchronization and Lock Contention** - Synchronized vs atomic operations
5. **False Sharing and Cache Line Contention** - Padding and cache optimization

## Quick Start

### Build
```bash
mvn clean install
```

### Run Individual Problems
```bash
# Run GC Pauses test
mvn exec:java -Dexec.mainClass="com.latency.lab.problems.Problem01_GCPauses"

# Run JIT Warm-up test
mvn exec:java -Dexec.mainClass="com.latency.lab.problems.Problem02_JITWarmup"

# Run Excessive Allocation test
mvn exec:java -Dexec.mainClass="com.latency.lab.problems.Problem03_ExcessiveAllocation"

# Run Synchronization test
mvn exec:java -Dexec.mainClass="com.latency.lab.problems.Problem04_SynchronizationContention"

# Run False Sharing test
mvn exec:java -Dexec.mainClass="com.latency.lab.problems.Problem05_FalseSharing"
```

## Output Format

Each problem test produces latency statistics:

```
============================================================
Problem: GC Pauses - Problematic Version
============================================================
Total Operations: 10,000
Mean:             245.32 ns (0.245 μs)
Median (p50):     150 ns (0.150 μs)
p90:              350 ns (0.350 μs)
p99:              1,250 ns (1.250 μs)
p99.9:            8,500 ns (8.500 μs)
p99.99:           125,000 ns (125.000 μs)
Max:              250,000 ns (250.000 μs)
============================================================
```

## Key Utilities

### LatencyHistogram
Provides detailed latency analysis with percentile breakdowns using HdrHistogram.

```java
LatencyHistogram histogram = new LatencyHistogram();
long start = System.nanoTime();
// ... operation ...
long latency = System.nanoTime() - start;
histogram.recordLatency(latency);
histogram.printSummary("Operation Name");
```

### GCMonitor
Tracks GC collections and pause times.

```java
GCMonitor gc = new GCMonitor();
gc.start();
// ... operations ...
gc.report();
```

## JVM Tuning Tips

### For Low Latency Workloads
```bash
# Use ZGC (Java 15+)
java -XX:+UseZGC ...

# Use Shenandoah (Java 12+)
java -XX:+UseShenandoahGC ...

# G1 tuning
java -XX:+UseG1GC -XX:MaxGCPauseMillis=10 ...

# Disable biased locking (can cause latency anomalies)
java -XX:-UseBiasedLocking ...

# Enable always pre-touch (eliminates first-touch delays)
java -XX:+AlwaysPreTouch ...
```

## Measurement Best Practices

1. **Warm-up Phase** - Always run warm-up iterations before measuring
2. **Percentile Analysis** - Focus on p99 and p99.9, not just mean/median
3. **Multiple Runs** - Execute tests multiple times for consistency
4. **Isolated Testing** - Test one problem at a time to avoid interference
5. **System Quiescence** - Run on quiet systems to minimize external noise

## Performance Goals

- **Ultra-Low Latency**: < 100 μs (microseconds)
- **Very Low Latency**: < 1 ms (millisecond)
- **Low Latency**: < 10 ms

## Dependencies

- **JMH** - Benchmarking framework
- **HdrHistogram** - Latency histogram recording
- **Disruptor** - Lock-free ring buffer
- **Log4j2** - Async logging

## License

MIT License