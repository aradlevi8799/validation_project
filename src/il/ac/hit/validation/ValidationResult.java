package il.ac.hit.validation;

import java.util.Optional;

/**
 * Represents the outcome of a single validation check.
 *
 * <p>Every validation rule (expressed as a {@link UserValidation}) returns an
 * instance of this interface when applied to a {@link User}.  Concrete
 * implementations are {@link Valid} (check passed) and {@link Invalid} (check
 * failed).</p>
 *
 * @see Valid
 * @see Invalid
 */
public interface ValidationResult {

    // Implementations: Valid (pass) and Invalid (fail with reason)

    /**
     * Returns {@code true} when the validation passed, {@code false} otherwise.
     *
     * @return whether this result represents a passing validation
     */
    boolean isValid();

    /**
     * Returns the human-readable reason for a failed validation.
     *
     * <p>Returns {@link Optional#empty()} when the validation passed, because
     * there is no failure reason to report.</p>
     *
     * @return an {@link Optional} containing the failure message, or empty if valid
     */
    Optional<String> getReason();
}
