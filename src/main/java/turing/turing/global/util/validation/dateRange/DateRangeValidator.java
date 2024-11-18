package turing.turing.global.util.validation.dateRange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startField = value.getClass().getDeclaredField(startDateField);
            Field endField = value.getClass().getDeclaredField(endDateField);

            startField.setAccessible(true);
            endField.setAccessible(true);

            LocalDate startDate = (LocalDate) startField.get(value);
            LocalDate endDate = (LocalDate) endField.get(value);

            if (startDate == null || endDate == null) {
                return true;
            }

            if (endDate.isBefore(startDate)) {
                throw new RestApiException(CommonErrorCode.END_DATE_BEFORE_START_DATE);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
