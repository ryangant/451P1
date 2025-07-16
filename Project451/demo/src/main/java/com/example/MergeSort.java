package com.example;

public class MergeSort extends AbstractSort {
    private int[] aux;

    @Override
    public void sort(int[] a) throws UnsortedException 
    {
        startSort();
        aux = new int[a.length];
        msort(a, 0, a.length - 1);
        endSort(a);
    }

    private void msort(int[] a, int lo, int hi) 
    {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        msort(a, lo, mid);
        msort(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }

    private void merge(int[] a, int lo, int mid, int hi) 
    {
        for (int k = lo; k <= hi; k++) 
        {
            aux[k] = a[k];
        }

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) 
        {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (aux[j] < aux[i]) 
            {
                incrementCount();
                a[k] = aux[j++];
            } else {
                incrementCount();
                a[k] = aux[i++];
            }
        }
    }
}
