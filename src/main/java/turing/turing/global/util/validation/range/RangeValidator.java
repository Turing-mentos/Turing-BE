package turing.turing.global.util.validation.range;

public interface RangeValidator<T> {

    boolean isValid(T start, T end);
}
