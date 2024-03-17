package com.lab2.function;

import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.lab2.functions.Ln;
import com.lab2.functions.Log;
import com.lab2.functions.Csc;
import com.lab2.functions.Sin;

import java.math.BigDecimal;
import java.math.MathContext;

public class FunctionsSystem implements Calculating {

  private final Sin sin;
  private final Ln ln;
  private final Log log3;
  private final Log log2;
  private final Log log5;
  private final Csc csc;

  public FunctionsSystem() {
    this.sin = new Sin();
    this.ln = new Ln();
    this.csc = new Csc();
    this.log3 = new Log(3);
    this.log2 = new Log(2);
    this.log5 = new Log(5);
  }

  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision) {
    final MathContext mc = new MathContext(DECIMAL128.getPrecision(), HALF_EVEN);
    final BigDecimal correctedX = x.remainder(BigDecimalMath.pi(mc).multiply(new BigDecimal(2)));
    if (x.compareTo(ZERO) <= 0) {
      return sin.calculate(correctedX, precision)
              .add(csc.calculate(correctedX, precision))
              .setScale(precision.scale(), HALF_EVEN);
    } else {
      return ((((log2.calculate(correctedX, precision)
              .divide(log3.calculate(correctedX, precision), mc))
              .subtract(ln.calculate(correctedX, precision)))
              .subtract(ln.calculate(correctedX, precision).pow(2)))
              .add(log2.calculate(correctedX, precision)))
              .multiply(log5.calculate(correctedX, precision))
              .setScale(precision.scale(), HALF_EVEN);
    }
  }

}
