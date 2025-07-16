package com.example;

public abstract class AbstractSort 
{

    private long   startTime;
    private long   elapsedTime;
    private int    opCount;

    public abstract void sort(int[] a) throws UnsortedException;

    protected final void startSort() 
    {
        opCount   = 0;
        startTime = System.nanoTime();
    }
    protected final void endSort(int[] a) throws UnsortedException 
    {
        elapsedTime = System.nanoTime() - startTime;
        if (!isSorted(a)) throw new UnsortedException("Array not sorted");
    }
    protected final void incrementCount() { opCount++; }

    public final int  getCount() { return opCount; }
    public final long getTime()  { return elapsedTime; }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
        return true;
    }
}
