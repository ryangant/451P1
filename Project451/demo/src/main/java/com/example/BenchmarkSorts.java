package com.example;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BenchmarkSorts 
{

    private static final int DATASET_COUNT = 12;
    private static final int RUNS_PER_SIZE = 40;
    private static final int START_SIZE    = 100;
    private static final int STEP_SIZE     = 100;

    public static void main(String[] args) throws Exception 
    {
        AbstractSort shellSort = new ShellSort();
        AbstractSort mergeSort = new MergeSort();

        warmUpJVM(shellSort, mergeSort);

        try 
        (
            PrintWriter shellOut = new PrintWriter("shellsort.dat");
            PrintWriter mergeOut = new PrintWriter("mergesort.dat")
        ) {
            for (int i = 0; i < DATASET_COUNT; i++) 
            {
                int size = START_SIZE + (i * STEP_SIZE);

                runBenchmark(shellSort, size, shellOut);
                runBenchmark(mergeSort, size, mergeOut);

                System.out.printf("Finished size %,d%n", size);
            }
        }

        System.out.println("Benchmark complete. Files written.");
    }

    private static void runBenchmark(AbstractSort sorter, int size, PrintWriter out) throws Exception 
    {
        List<Integer> opCounts = new ArrayList<>();
        List<Long> runtimes = new ArrayList<>();

        StringBuilder rawLine = new StringBuilder();
        rawLine.append(size);

        for (int i = 0; i < RUNS_PER_SIZE; i++) 
        {
            int[] data = generateRandomInts(size);
            sorter.sort(data);

            int count = sorter.getCount();
            long time = sorter.getTime();

            opCounts.add(count);
            runtimes.add(time);

            rawLine.append(" ").append(count).append(" ").append(time);
        }

        out.println(rawLine.toString());
        out.flush();

        double avgCount = average(opCounts);
        double cvCount = coefficientOfVariation(opCounts, avgCount);
        double avgTime = average(runtimes);
        double cvTime = coefficientOfVariation(runtimes, avgTime);

        try (PrintWriter summary = new PrintWriter(new FileWriter(
                sorter.getClass().getSimpleName().toLowerCase() + "_summary.dat", true))) {
            summary.printf("%d %.4f %.1f%% %.4f %.1f%%%n",
                    size, avgCount, cvCount, avgTime, cvTime);
        }
    }

    private static void warmUpJVM(AbstractSort... sorters) throws Exception {
        final int WARM_UP_RUNS = 10;
        final int SIZE = 20000;

        for (int i = 0; i < WARM_UP_RUNS; i++) 
        {
            int[] data = generateRandomInts(SIZE);
            for (AbstractSort sorter : sorters) 
            {
                sorter.sort(data.clone());
            }
        }
        System.out.println("JVM warm-up done.");
    }

    private static int[] generateRandomInts(int n) {
        Random rand = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = rand.nextInt();
        return array;
    }

    private static double average(List<? extends Number> list) 
    {
        double sum = 0;
        for (Number val : list) sum += val.doubleValue();
        return sum / list.size();
    }

    private static double coefficientOfVariation(List<? extends Number> list, double mean) 
    {
        if (mean == 0) return 0;
        double sumSquaredDiffs = 0;
        for (Number val : list) {
            double diff = val.doubleValue() - mean;
            sumSquaredDiffs += diff * diff;
        }
        double stdDev = Math.sqrt(sumSquaredDiffs / list.size());
        return (stdDev / mean) * 100.0;
    }
}
