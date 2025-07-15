package turing.turing.global.util.validation.range.number;

import turing.turing.global.util.validation.range.RangeValidator;

public class NumberRangeValidator implements RangeValidator<Integer> {

    @Override
    public boolean isValid(Integer start, Integer end) {
        return !(end < start);
    }
}
