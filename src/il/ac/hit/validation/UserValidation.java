package il.ac.hit.validation;

import java.util.function.Function;

/**
 * A functional interface representing a single validation rule for a {@link User}.
 *
 * <p>Extends {@link Function}{@code <User, ValidationResult>} so that every
 * validation rule is simply a function: receive a user, return a result.</p>
 *
 * <p><b>Design pattern — Combinator:</b> this interface is the heart of the library.
 * Simple, atomic rules (e.g. {@link #emailEndsWithIL()}) are created via static
 * factory methods.  Complex rules are then assembled from simpler ones using the
 * combinator methods {@link #and}, {@link #or}, {@link #xor}, {@link #all}, and
 * {@link #none} — each of which returns a brand-new {@link UserValidation} that
 * wraps the originals.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * UserValidation rule = UserValidation.emailLengthBiggerThan10()
 *                                     .and(UserValidation.emailEndsWithIL());
 *
 * ValidationResult result = rule.apply(user);
 * if (result.isValid()) { ... }
 * }</pre>
 */
@FunctionalInterface
public interface UserValidation extends Function<User, ValidationResult> {

    // ── Instance combinator methods ──────────────────────────────────────────

    /**
     * Returns a new validation that passes only when <em>both</em> this rule
     * and {@code other} pass (logical AND).
     *
     * <p>Uses short-circuit evaluation: if this rule fails, {@code other} is
     * never evaluated and the failing result is returned immediately.</p>
     *
     * @param other the second rule to combine with
     * @return a combined {@link UserValidation} representing {@code this AND other}
     */
    default UserValidation and(UserValidation other) {
        return user -> {
            // Short-circuit: return the first failure without evaluating 'other'
            ValidationResult result = this.apply(user);
            if (!result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    /**
     * Returns a new validation that passes when <em>at least one</em> of this rule
     * or {@code other} passes (logical OR).
     *
     * <p>Uses short-circuit evaluation: if this rule passes, {@code other} is
     * never evaluated.</p>
     *
     * @param other the second rule to combine with
     * @return a combined {@link UserValidation} representing {@code this OR other}
     */
    default UserValidation or(UserValidation other) {
        return user -> {
            // Short-circuit: return immediately if this rule already passes
            ValidationResult result = this.apply(user);
            if (result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    /**
     * Returns a new validation that passes when <em>exactly one</em> of this rule
     * or {@code other} passes (logical XOR).
     *
     * <p>Both rules are always evaluated so their individual results can be compared.</p>
     *
     * @param other the second rule to combine with
     * @return a combined {@link UserValidation} representing {@code this XOR other}
     */
    default UserValidation xor(UserValidation other) {
        return user -> {
            ValidationResult firstResult  = this.apply(user);
            ValidationResult secondResult = other.apply(user);

            // XOR passes only when the two outcomes differ
            boolean firstValid  = firstResult.isValid();
            boolean secondValid = secondResult.isValid();

            if (firstValid ^ secondValid) {
                return new Valid();
            }
            return new Invalid("XOR failed: both conditions produced the same result");
        };
    }

    // ── Static combinator methods (varargs) ──────────────────────────────────

    /**
     * Returns a validation that passes only when <em>every</em> supplied rule passes.
     *
     * <p>Evaluates rules in order and stops at the first failure.</p>
     *
     * @param validations the rules to evaluate (varargs)
     * @return a combined {@link UserValidation} that requires all rules to pass
     */
    static UserValidation all(UserValidation... validations) {
        return user -> {
            // Fail fast: return the first failing result
            for (UserValidation validation : validations) {
                ValidationResult result = validation.apply(user);
                if (!result.isValid()) {
                    return result;
                }
            }
            return new Valid();
        };
    }

    /**
     * Returns a validation that passes only when <em>none</em> of the supplied
     * rules passes.
     *
     * <p>Evaluates rules in order and stops as soon as one passes.</p>
     *
     * @param validations the rules to evaluate (varargs)
     * @return a combined {@link UserValidation} that requires all rules to fail
     */
    static UserValidation none(UserValidation... validations) {
        return user -> {
            // Fail as soon as any rule passes
            for (UserValidation validation : validations) {
                ValidationResult result = validation.apply(user);
                if (result.isValid()) {
                    return new Invalid("NONE failed: at least one condition was fulfilled");
                }
            }
            return new Valid();
        };
    }

    // ── Static factory methods — built-in validation rules ───────────────────

    /**
     * Returns a rule that checks whether the user's email ends with {@code "il"}.
     *
     * @return a {@link UserValidation} for the email-ends-with-IL rule
     */
    static UserValidation emailEndsWithIL() {
        return user -> user.getEmail().endsWith("il")
                ? new Valid()
                : new Invalid("Email does not end with 'il'");
    }

    /**
     * Returns a rule that checks whether the user's email length is greater than 10.
     *
     * @return a {@link UserValidation} for the email-length-bigger-than-10 rule
     */
    static UserValidation emailLengthBiggerThan10() {
        return user -> user.getEmail().length() > 10
                ? new Valid()
                : new Invalid("Email length is not greater than 10");
    }

    /**
     * Returns a rule that checks whether the user's password length is greater than 8.
     *
     * @return a {@link UserValidation} for the password-length-bigger-than-8 rule
     */
    static UserValidation passwordLengthBiggerThan8() {
        return user -> user.getPassword().length() > 8
                ? new Valid()
                : new Invalid("Password length is not greater than 8");
    }

    /**
     * Returns a rule that checks whether the user's password contains only
     * ASCII letters and digits (no special characters).
     *
     * @return a {@link UserValidation} for the password-includes-letters-numbers-only rule
     */
    static UserValidation passwordIncludesLettersNumbersOnly() {
        // [a-zA-Z0-9]+ matches only letters and digits, nothing else
        return user -> user.getPassword().matches("[a-zA-Z0-9]+")
                ? new Valid()
                : new Invalid("Password must contain letters and numbers only");
    }

    /**
     * Returns a rule that checks whether the user's password contains the {@code '$'} character.
     *
     * @return a {@link UserValidation} for the password-includes-dollar-sign rule
     */
    static UserValidation passwordIncludesDollarSign() {
        return user -> user.getPassword().contains("$")
                ? new Valid()
                : new Invalid("Password must include the '$' character");
    }

    /**
     * Returns a rule that checks whether the user's password differs from the username.
     *
     * @return a {@link UserValidation} for the password-is-different-from-username rule
     */
    static UserValidation passwordIsDifferentFromUsername() {
        return user -> !user.getPassword().equals(user.getUsername())
                ? new Valid()
                : new Invalid("Password must be different from the username");
    }

    /**
     * Returns a rule that checks whether the user's age is greater than 18.
     *
     * @return a {@link UserValidation} for the age-bigger-than-18 rule
     */
    static UserValidation ageBiggerThan18() {
        return user -> user.getAge() > 18
                ? new Valid()
                : new Invalid("Age must be greater than 18");
    }

    /**
     * Returns a rule that checks whether the user's username length is greater than 8.
     *
     * @return a {@link UserValidation} for the username-length-bigger-than-8 rule
     */
    static UserValidation usernameLengthBiggerThan8() {
        return user -> user.getUsername().length() > 8
                ? new Valid()
                : new Invalid("Username length must be greater than 8");
    }
}
