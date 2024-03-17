package com.lab2;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

import com.lab2.functions.Csc;
import com.lab2.functions.Sin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CscTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Mock
    private Sin mockSin;
    @Spy
    private Sin spySin;

    @Test
    void shouldCallSinFunction() {
        // Arrange
        final Csc csc = new Csc(spySin);

        // Act
        csc.calculate(ONE, DEFAULT_PRECISION);

        // Assert
        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void shouldCalculateWithMockSin() {
        // Arrange
        final BigDecimal arg = new BigDecimal(5);
        final Csc csc = new Csc(mockSin);
        final BigDecimal expectedResult = new BigDecimal("-1.0428");

        // Act
        when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("-0.95892427"));

        // Assert
        assertEquals(expectedResult, csc.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForZero() {
        // Arrange
        final Csc csc = new Csc();

        // Act + Assert
        assertThrows(ArithmeticException.class, () -> csc.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPi() {
        // Arrange
        final Csc csc = new Csc();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg = BigDecimalMath.pi(mc);

        // Act + Assert
        assertThrows(ArithmeticException.class, () -> csc.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForOne() {
        // Arrange
        final Csc csc = new Csc();
        final BigDecimal expected = new BigDecimal("1.1884");

        // Act + Assert
        assertEquals(expected, csc.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPeriodic() {
        final Csc csc = new Csc();
        final BigDecimal expected = new BigDecimal("1.1288");

        // Act + Assert
        assertEquals(expected, csc.calculate(new BigDecimal(134), DEFAULT_PRECISION));
    }
}

