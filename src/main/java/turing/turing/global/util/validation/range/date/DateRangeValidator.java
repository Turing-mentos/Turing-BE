package turing.turing.global.util.validation.range.date;

import java.time.LocalDate;
import turing.turing.global.util.validation.range.RangeValidator;

public class DateRangeValidator implements RangeValidator<LocalDate> {

    @Override
    public boolean isValid(LocalDate start, LocalDate end) {
        return !end.isBefore(start);
    }
}
