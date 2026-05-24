import il.ac.hit.validation.*;

/**
 * Interactive demo showing all three design patterns in action.
 * Designed for the video recording.
 */
public class LiveDemo {

    public static void main(String[] args) {

        System.out.println("===============================================");
        System.out.println("        Validation Library - Live Demo         ");
        System.out.println("===============================================\n");

        // ── 1. Factory Method ────────────────────────────────────────────────
        System.out.println("-- 1. Factory Method Pattern --");

        User basic    = UserFactory.create("basic",    "alice99",  "alice@dev.co.il",   "Pass$2024",  22);
        User premium  = UserFactory.create("premium",  "bob1234",  "bob@corp.co.il",    "Secure$99",  30);
        User platinum = UserFactory.create("platinum", "charlie5", "charlie@vip.co.il", "VIP$pass1",  45);

        System.out.println("  basic    -> " + basic.getClass().getSimpleName()    + " | " + basic);
        System.out.println("  premium  -> " + premium.getClass().getSimpleName()  + " | " + premium);
        System.out.println("  platinum -> " + platinum.getClass().getSimpleName() + " | " + platinum);

        // ── 2. Template Method ───────────────────────────────────────────────
        System.out.println("\n-- 2. Template Method Pattern (sort) --");

        User[] users = { basic, premium, platinum };

        System.out.println("  Before sort:");
        for (User u : users) {
            System.out.println("    " + u.getUsername() + " (age " + u.getAge() + ")");
        }

        // The comparator is the "hook" — pluggable comparison step
        UserUtils.sort(users, (a, b) -> Integer.compare(a.getAge(), b.getAge()));

        System.out.println("  After sort by age (ascending):");
        for (User u : users) {
            System.out.println("    " + u.getUsername() + " (age " + u.getAge() + ")");
        }

        // ── 3. Combinator Pattern ────────────────────────────────────────────
        System.out.println("\n-- 3. Combinator Pattern --");

        User goodUser = new User("alice99", "alice@dev.co.il", "Pass$2024X", 22);
        User badUser  = new User("ali",     "x@y.com",         "123",        15);

        System.out.println("\n  Rule: emailEndsWithIL");
        printResult("goodUser", goodUser, UserValidation.emailEndsWithIL());
        printResult("badUser",  badUser,  UserValidation.emailEndsWithIL());

        System.out.println("\n  Rule: emailEndsWithIL AND passwordLengthBiggerThan8");
        ValidationResult combined = UserValidation.emailEndsWithIL()
                .and(UserValidation.passwordLengthBiggerThan8())
                .apply(goodUser);
        System.out.println("  goodUser -> " + status(combined));

        System.out.println("\n  Rule: all(emailEndsWithIL, ageBiggerThan18, passwordIncludesDollarSign)");
        ValidationResult allResult = UserValidation.all(
                UserValidation.emailEndsWithIL(),
                UserValidation.ageBiggerThan18(),
                UserValidation.passwordIncludesDollarSign()
        ).apply(badUser);
        System.out.println("  badUser -> " + status(allResult));

        System.out.println("\n  Rule: emailEndsWithIL OR ageBiggerThan18");
        // email fails ("x@y.com"), but age passes (25 > 18) -> OR passes
        ValidationResult orResult = UserValidation.emailEndsWithIL()
                .or(UserValidation.ageBiggerThan18())
                .apply(new User("user25", "x@y.com", "p", 25));
        System.out.println("  email fails, age 25 passes -> " + status(orResult));

        // ── Demo from assignment ─────────────────────────────────────────────
        System.out.println("\n-- Assignment demo code (exact) --");
        User user = new User("admin", "admin@#yzw.co.il", "abc123", 34);
        ValidationResult result = UserValidation.emailLengthBiggerThan10()
                .and(UserValidation.emailEndsWithIL())
                .apply(user);
        if (result.isValid()) {
            System.out.println("  User is valid");
        } else {
            System.out.println("  User is not valid");
        }

        System.out.println("\n===============================================");
        System.out.println("  Demo complete. All patterns verified.");
        System.out.println("===============================================");
    }

    /** Prints a labelled pass/fail line for a single rule applied to a user. */
    private static void printResult(String label, User user, UserValidation rule) {
        ValidationResult r = rule.apply(user);
        if (r.isValid()) {
            System.out.println("  " + label + " -> VALID");
        } else {
            System.out.println("  " + label + " -> INVALID: " + r.getReason().get());
        }
    }

    /** Returns a formatted status string from a ValidationResult. */
    private static String status(ValidationResult r) {
        return r.isValid() ? "VALID" : "INVALID: " + r.getReason().get();
    }
}
