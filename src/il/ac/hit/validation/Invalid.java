package il.ac.hit.validation;

import java.util.Optional;

/**
 * A {@link ValidationResult} that represents a failed validation.
 *
 * <p>Stores a human-readable reason string that explains why the check failed.
 * {@link #isValid()} always returns {@code false} and {@link #getReason()}
 * always returns a non-empty {@link Optional}.</p>
 */
public class Invalid implements ValidationResult {

    /** Human-readable explanation of why the validation failed. */
    private String reason;

    /**
     * Constructs a new Invalid result with the given failure reason.
     *
     * @param reason the message explaining why the validation failed
     */
    public Invalid(String reason) {
        // Route through setter — avoids direct field assignment
        setReason(reason);
    }

    /**
     * Returns {@code false} — this result always represents a failed check.
     *
     * @return {@code false}
     */
    @Override
    public boolean isValid() {
        return false;
    }

    /**
     * Returns the failure reason wrapped in an {@link Optional}.
     *
     * @return an {@link Optional} containing the non-null failure message
     */
    @Override
    public Optional<String> getReason() {
        return Optional.of(reason);
    }

    /**
     * Sets the failure reason.
     *
     * @param reason the new failure message
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns a human-readable representation of this result.
     *
     * @return a string containing the failure reason
     */
    @Override
    public String toString() {
        return "Invalid{reason='" + reason + "'}";
    }
}
