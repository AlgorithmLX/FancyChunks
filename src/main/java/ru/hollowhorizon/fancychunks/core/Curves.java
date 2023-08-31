package ru.hollowhorizon.fancychunks.core;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum Curves {
    LINEAR(f -> f),
    EASE_OUT(f -> 1.0F - (1.0F - f) * (1.0F - f) * (1.0F - f));

    private final Function<Float, Float> calculate;

    Curves(UnaryOperator<Float> calculate) {
        this.calculate = calculate;
    }

    public Float calculate(Float in) {
        return this.calculate.apply(in);
    }
}