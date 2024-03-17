package com.lab2;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import com.lab2.functions.Ln;
import org.junit.jupiter.api.Test;

class LnTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.00001");

    @Test
    void shouldNotCalculateForZero() {
        // Arrange
        final Ln ln = new Ln();

        // Act + Assert
        assertThrows(ArithmeticException.class, () -> ln.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForOne() {
        // Arrange
        final Ln ln = new Ln();

        // Act + Assert
        assertEquals(ZERO, ln.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPositive() {
        // Arrange
        final Ln ln = new Ln();
        final BigDecimal expected = new BigDecimal("1.38629");

        // Act + Assert
        assertEquals(expected, ln.calculate(new BigDecimal(4), DEFAULT_PRECISION));
    }
}
