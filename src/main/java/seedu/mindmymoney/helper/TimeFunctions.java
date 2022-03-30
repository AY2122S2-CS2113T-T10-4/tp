package seedu.mindmymoney.helper;

import seedu.mindmymoney.MindMyMoneyException;
import seedu.mindmymoney.constants.ValidationRegexTypes;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Container for functions needed to calculate and format time.
 */
public class TimeFunctions {

    /**
     * Temporary day string to be appended to user input of yyyy-MM to obtain the proper datetime format of LocalDate.
     */
    public static final String TEMPORARY_DAY = "-01";

    /**
     * Converts time in the yyyy-MM format to the MMM yyyy format.
     *
     * @param inputTime Input time in the yyyy-MM format.
     * @return Time in the MMM yyyy format.
     * @throws MindMyMoneyException when inputTime is not in the yyyy-MM format or inputTime is null.
     */
    public static String convertTime(String inputTime) throws MindMyMoneyException {
        if (inputTime == null) {
            throw new MindMyMoneyException("Time cannot be null!");
        }
        try {
            LocalDate date;
            inputTime = inputTime.strip() + TEMPORARY_DAY;
            date = LocalDate.parse(inputTime);
            return date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
        } catch (DateTimeException e) {
            throw new MindMyMoneyException("Input the correct date time format!");
        }
    }

    /**
     * Checks if date input format is valid.
     *
     * @param input The string of the date input.
     * @return true if format is valid, false otherwise.
     */
    public static boolean isValidInput(String input) {
        if (input.matches(ValidationRegexTypes.VALIDATION_REGEX_D)
                || input.matches(ValidationRegexTypes.VALIDATION_REGEX_M)
                || input.matches(ValidationRegexTypes.VALIDATION_REGEX_Y)) {
            return true;
        }
        return false;

    }
}
