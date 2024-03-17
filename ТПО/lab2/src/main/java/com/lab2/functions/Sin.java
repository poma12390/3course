package com.lab2.functions;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;

import com.lab2.function.AbstractFunction;

import java.math.BigDecimal;

public class Sin extends AbstractFunction {

  public Sin() {
    super();
  }

  @Override
  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
      throws ArithmeticException {

    double X = x.doubleValue();

    double PI2 = Math.PI * 2;
    int i = 0;
    BigDecimal sum = new BigDecimal(0), prev;

    if (X >= 0) {
      while (X > PI2) {
        X -= PI2;
      }
    } else if (X < 0){
      while (X < PI2) {
        X += PI2;
      }
    }

    do {
      prev = sum;
      sum = sum.add(minusOnePow(i).multiply(prod(X, 2 * i + 1)));
      i++;
    } while (new BigDecimal("0.1").pow(precision.scale()).compareTo(prev.subtract(sum).abs()) < 0);

    return sum.setScale(precision.scale(), HALF_EVEN);
  }

  private static BigDecimal minusOnePow(int n){
    return BigDecimal.valueOf(1 - (n % 2) * 2);
  }

  private static BigDecimal prod(double x, int n){
    BigDecimal accum = new BigDecimal(1);

    for (int i = 1; i <= n; i++){
      accum = accum.multiply( new BigDecimal(x / i));
    }

    return accum;
  }
}
