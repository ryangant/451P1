package com.example;

public class ShellSort extends AbstractSort 
{

    @Override
    public void sort(int[] a) throws UnsortedException 
    {
        startSort();

        int n = a.length;
        int h = 1;
        while (h < n / 3) h = 3 * h + 1;

        while (h >= 1) {
            for (int i = h; i < n; i++) 
            {
                int tmp = a[i];
                int j = i;

                while (j >= h) {
                    incrementCount();
                    if (a[j - h] > tmp) {
                        a[j] = a[j - h];
                        j -= h;
                    } else {
                        break;
                    }
                }
                a[j] = tmp;
            }
            h /= 3;
        }

        endSort(a);
    }
}
