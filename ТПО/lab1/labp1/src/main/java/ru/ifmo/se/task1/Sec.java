package ru.ifmo.se.task1;

public class Sec {
    public static double calc(double x, int n) {
        x = x % (Math.PI * 2);
        if (x < 0) {
            x += Math.PI * 2;
        }

        double term = 1;
        double sum = 1;
        for (int i = 1; i < n; i++) {
            term *= -1 * x * x / (2 * i * (2 * i - 1));
            sum += term;
        }
        return 1/sum;
    }
}
