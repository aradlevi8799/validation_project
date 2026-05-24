package il.ac.hit.validation;

import java.util.Comparator;

/**
 * Utility class providing operations on arrays of {@link User} objects.
 *
 * <p><b>Design pattern — Template Method:</b> the {@link #sort} method encodes a
 * fixed sorting algorithm (bubble sort) whose comparison step is intentionally left
 * open.  The caller fills in that step by passing a {@link Comparator}{@code <User>}.
 * In classic Template Method the open step is an abstract method overridden in a
 * subclass; here the equivalent is the {@code comparator} parameter, which Java's
 * {@link Comparator} interface captures as a first-class object — an equally valid
 * realisation of the pattern.</p>
 */
public class UserUtils {

    /**
     * Sorts the given array of users in place using the supplied comparator.
     *
     * <p>The algorithm skeleton (bubble sort) is fixed inside this method.
     * The single variable step — comparing two adjacent users — is delegated to
     * {@code comparator}, making it the Template Method "hook".</p>
     *
     * @param users      the array of users to sort; modified in place
     * @param comparator defines the ordering between any two users
     */
    public static void sort(User[] users, Comparator<User> comparator) {
        int n = users.length;

        // Outer pass: each pass bubbles the largest unsorted element to its final position
        for (int i = 0; i < n - 1; i++) {
            // Inner pass: compare adjacent pairs
            for (int j = 0; j < n - i - 1; j++) {
                // The comparator is the "hook" — pluggable comparison logic
                if (comparator.compare(users[j], users[j + 1]) > 0) {
                    // Swap users[j] and users[j+1]
                    User temp = users[j];
                    users[j] = users[j + 1];
                    users[j + 1] = temp;
                }
            }
        }
    }
}
