package turing.turing.global.util.validation.range;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import turing.turing.global.util.validation.range.date.DateRangeValidator;
import turing.turing.global.util.validation.range.number.NumberRangeValidator;

public class RangeValidatorFactory {

    private static final Map<Class<?>, RangeValidator<?>> validators = new HashMap<>();

    static {
        validators.put(LocalDate.class, new DateRangeValidator());
        validators.put(Integer.class, new NumberRangeValidator());
    }

    @SuppressWarnings("unchecked")
    public static <T> RangeValidator<Object> getValidator(Class<T> type) {
        RangeValidator<?> validator = validators.get(type);
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for type: " + type.getName());
        }
        return (RangeValidator<Object>) validator;
    }
}
