package turing.turing.global.util.validation.range;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class DynamicRangeValidator implements ConstraintValidator<ValidRange, Object> {

    private String startField;
    private String endField;
    private Class<?> type;

    @Override
    public void initialize(ValidRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startField = value.getClass().getDeclaredField(this.startField);
            Field endField = value.getClass().getDeclaredField(this.endField);

            startField.setAccessible(true);
            endField.setAccessible(true);

            Object startValue = startField.get(value);
            Object endValue = endField.get(value);

            if (startValue == null || endValue == null) {
                return true;
            }

            RangeValidator<Object> validator = RangeValidatorFactory.getValidator(type);

            return validator.isValid(startValue, endValue);

        } catch (Exception e) {
            return false;
        }
    }
}
