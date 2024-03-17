package com.lab2;

import com.lab2.function.FunctionsSystem;
import com.lab2.functions.Ln;
import com.lab2.functions.Log;
import com.lab2.functions.Cos;
import com.lab2.functions.Csc;
import com.lab2.functions.Sin;

import java.io.IOException;
import java.math.BigDecimal;

public class App {

    public static void main(String[] args) throws IOException {
        final Cos cos = new Cos();
        CsvWriter.write(
                "csv/cos.csv",
                cos,
                new BigDecimal(-1),
                new BigDecimal(1),
                new BigDecimal("0.1"),
                new BigDecimal("0.0000000001"));

        final Sin sin = new Sin();
        CsvWriter.write(
                "csv/sin.csv",
                sin,
                new BigDecimal(-1),
                new BigDecimal(1),
                new BigDecimal("0.1"),
                new BigDecimal("0.0000000001"));

        final Ln ln = new Ln();
        CsvWriter.write(
                "csv/ln.csv",
                ln,
                new BigDecimal(1),
                new BigDecimal(20),
                new BigDecimal("0.1"),
                new BigDecimal("0.0000000001"));

        final Log log3 = new Log(3);
        CsvWriter.write(
                "csv/log3.csv",
                log3,
                new BigDecimal(1),
                new BigDecimal(20),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000000001"));

        final Log log5 = new Log(5);
        CsvWriter.write(
                "csv/log5.csv",
                log5,
                new BigDecimal(1),
                new BigDecimal(20),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000000001"));

        final Log log10 = new Log(10);
        CsvWriter.write(
                "csv/log10.csv",
                log10,
                new BigDecimal(1),
                new BigDecimal(20),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000000001"));

        final Csc csc = new Csc();
        CsvWriter.write(
                "csv/csc.csv",
                csc,
                new BigDecimal(-10),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000000001"));

        final FunctionsSystem func = new FunctionsSystem();
        CsvWriter.write(
                "csv/func.csv",
                func,
                new BigDecimal(-5),
                new BigDecimal(5),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000000001"));
    }
}
