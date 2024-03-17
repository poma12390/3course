package com.lab2;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

import com.lab2.functions.Sin;
import org.junit.jupiter.api.Test;

class SinTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Test
    void shouldCalculateForZero() {
        // Arrange
        final Sin sin = new Sin();
        assertEquals(ZERO.setScale(4, HALF_EVEN), sin.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPiDividedByTwo() {
        // Arrange
        final Sin sin = new Sin();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());

        // Act
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        // Act + Assert
        assertEquals(
                ONE.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), sin.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForOne() {
        // Arrange
        final Sin sin = new Sin();
        final BigDecimal expected = new BigDecimal("0.8415");

        // Act + Assert
        assertEquals(expected, sin.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPeriodic() {
        // Arrange
        final Sin sin = new Sin();
        final BigDecimal expected = new BigDecimal("0.0972");

        // Act + Assert
        assertEquals(expected, sin.calculate(new BigDecimal(-113), DEFAULT_PRECISION));
    }
}
