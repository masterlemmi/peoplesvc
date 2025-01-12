package com.lemmi.tools.people._interviews.questions;

//https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
public class nthFibonacci {

    static int fibRecursive(int n) {
        if (n <= 1)
            return n;
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }

    public static void main(String args[]) {
        int n = 9;
        System.out.println(
                n + "th Fibonacci Number: " + fibDynamic(n));
    }


    static int fibDynamic(int n)
    {
        /* Declare an array to store Fibonacci numbers. */
        int f[] = new int[n + 2]; // 1 extra to handle case, n = 0
        int i;

        /* 0th and 1st number of the series are 0 and 1*/
        f[0] = 0;
        f[1] = 1;

        for (i = 2; i <= n; i++) {
            /* Add the previous 2 numbers in the series
              and store it */
            f[i] = f[i - 1] + f[i - 2];
        }

        return f[n];
    }


};