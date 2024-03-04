import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;
import ru.ifmo.se.task1.Sec;

import java.util.List;

public class SecTest {

    private static final double DELTA = 0.0001;
    private static final double INF_THRESHOLD = 1e14;

    @ParameterizedTest
    @DisplayName("Check Secant Values from CSV")
    @CsvFileSource(resources = "/values.csv", numLinesToSkip = 1, delimiter = ';')
    void checkSecantValuesFromCsv(double arg, double expected) {
        Assertions.assertEquals(expected, Sec.calc(arg, 100), DELTA);
    }

    @ParameterizedTest
    @DisplayName("Check Secant Tabular Values")
    @MethodSource("tabularValuesSource")
    void checkSecantTabularValues(double arg, double expected) {
        Assertions.assertEquals(expected, Sec.calc(arg, 100), DELTA);
    }

    private static List<Arguments> tabularValuesSource() {
        return List.of(
                Arguments.of(-2 * Math.PI, 1 / Math.cos(-2 * Math.PI)),
                Arguments.of(-Math.PI, 1 / Math.cos(-Math.PI)),
                Arguments.of(0, 1 / Math.cos(0)),
                Arguments.of(Math.PI, 1 / Math.cos(Math.PI)),
                Arguments.of(2 * Math.PI, 1 / Math.cos(2 * Math.PI))
        );
    }

    @ParameterizedTest
    @DisplayName("Check Secant for Undefined Values")
    @MethodSource("undefinedValuesSource")
    void checkSecantForUndefinedValues(double arg) {
        Assertions.assertTrue(Math.abs(Sec.calc(arg, 100)) > INF_THRESHOLD);
    }

    private static List<Arguments> undefinedValuesSource() {
        return List.of(
                Arguments.of(-1.5 * Math.PI),
                Arguments.of(-0.5 * Math.PI),
                Arguments.of(0.5 * Math.PI),
                Arguments.of(1.5 * Math.PI)
        );
    }
}
