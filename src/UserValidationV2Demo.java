import il.ac.hit.validation.User;
import il.ac.hit.validation.UserValidation;
import il.ac.hit.validation.ValidationResult;

/**
 * Demonstrates the validation library as described in the assignment.
 *
 * <p>Creates a user, builds two individual validation rules, combines them
 * with {@code and}, applies the combined rule, and prints the outcome.</p>
 */
public class UserValidationV2Demo {

    /**
     * Entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Build a user: username="admin", email="admin@#yzw.co.il", password="abc123", age=34
        User user = new User("admin", "admin@#yzw.co.il", "abc123", 34);

        // Rule 1: email length must be > 10  (length of "admin@#yzw.co.il" = 17 → passes)
        UserValidation validation1 = UserValidation.emailLengthBiggerThan10();

        // Rule 2: email must end with "il"  ("admin@#yzw.co.il" ends with "il" → passes)
        UserValidation validation2 = UserValidation.emailEndsWithIL();

        // Combine with AND — both must pass
        ValidationResult result = validation1.and(validation2).apply(user);

        // Print the outcome
        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            // isValid() == false → getReason() is guaranteed to be present
            System.out.println("User is not valid: " + result.getReason().orElse("unknown reason"));
        }
    }
}
