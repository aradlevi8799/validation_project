package il.ac.hit.validation;

import java.util.Optional;

/**
 * A {@link ValidationResult} that represents a passing validation.
 *
 * <p>{@link #isValid()} always returns {@code true} and {@link #getReason()}
 * always returns an empty {@link Optional} because there is no failure message
 * to report.</p>
 */
public class Valid implements ValidationResult {

    /**
     * Constructs a new Valid result.
     */
    public Valid() {
        // No state to initialise
    }

    /**
     * Returns {@code true} — this result always represents a passed check.
     *
     * @return {@code true}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Returns an empty Optional — a passing result carries no failure message.
     *
     * @return {@link Optional#empty()}
     */
    @Override
    public Optional<String> getReason() {
        // No reason to report when validation passes
        return Optional.empty();
    }

    /**
     * Returns a human-readable representation of this result.
     *
     * @return {@code "Valid"}
     */
    @Override
    public String toString() {
        return "Valid";
    }
}
