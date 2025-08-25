package util;

public class InputValidator {
    public static boolean validateId(String id) {
        return id != null && id.matches("[0-9]{4,}");
    }

    public static boolean validateCourseCode(String code) {
        return code != null && code.matches("[A-Z]{2,4}[0-9]{3}");
    }

    public static boolean validateName(String name) {
        return name != null && name.length() >= 2;
    }
}
