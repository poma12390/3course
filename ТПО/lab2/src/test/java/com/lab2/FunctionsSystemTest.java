package com.lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import com.lab2.function.FunctionsSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FunctionsSystemTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.00000001");

    @Test
    void shouldNotAcceptNullArgument() {
        // Arrange
        final FunctionsSystem system = new FunctionsSystem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> system.calculate(null, DEFAULT_PRECISION));
    }

    @Test
    void shouldNotAcceptNullPrecision() {
        // Arrange
        final FunctionsSystem system = new FunctionsSystem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> system.calculate(new BigDecimal(-2), null));
    }

    @ParameterizedTest
    @MethodSource("illegalPrecisions")
    void shouldNotAcceptIncorrectPrecisions(final BigDecimal precision) {
        // Arrange
        final FunctionsSystem system = new FunctionsSystem();

        // Act + Assert
        assertThrows(ArithmeticException.class, () -> system.calculate(new BigDecimal(-2), precision));
    }


    @Test
    void shouldCalculateForNegativeValue() {
        // Arrange
        final FunctionsSystem system = new FunctionsSystem();
        final BigDecimal expected = new BigDecimal("-0.29283691");

        // Act + Assert
        assertEquals(expected, system.calculate(new BigDecimal(5), DEFAULT_PRECISION));
    }

    @Test
    void shouldCalculateForPositiveValue() {
        // Arrange
        final FunctionsSystem system = new FunctionsSystem();
        final BigDecimal expected = new BigDecimal("2.00175949");

        // Act + Assert
        assertEquals(expected, system.calculate(new BigDecimal(-5), DEFAULT_PRECISION));
    }

    private static List<Arguments> illegalPrecisions() {
        return List.of(
                Arguments.of(BigDecimal.valueOf(1)),
                Arguments.of(BigDecimal.valueOf(0)),
                Arguments.of(BigDecimal.valueOf(1.01)),
                Arguments.of(BigDecimal.valueOf(-0.01)));
    }
}
