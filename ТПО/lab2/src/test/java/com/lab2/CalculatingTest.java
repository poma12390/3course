package com.lab2;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import com.lab2.function.Calculating;
import com.lab2.functions.Ln;
import com.lab2.functions.Log;
import com.lab2.functions.Cos;
import com.lab2.functions.Sin;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CalculatingTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.000001");

  @ParameterizedTest
  @MethodSource("functions")
  void shouldNotAcceptNullArgument(final Calculating function) {
    // Arrange + Act + Assert
    assertThrows(NullPointerException.class, () -> function.calculate(null, DEFAULT_PRECISION));
  }

  @ParameterizedTest
  @MethodSource("functions")
  void shouldNotAcceptNullPrecision(final Calculating function) {
    // Arrange + Act + Assert
    assertThrows(NullPointerException.class, () -> function.calculate(ONE, null));
  }

  private static List<Arguments> functions() {
    return List.of(
        Arguments.of(new Sin()),
        Arguments.of(new Cos()),
        Arguments.of(new Ln()),
        Arguments.of(new Log(3)),
        Arguments.of(new Log(5)),
        Arguments.of(new Log(10)));
  }
}
