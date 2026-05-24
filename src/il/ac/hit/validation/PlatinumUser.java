package il.ac.hit.validation;

/**
 * Represents a platinum-tier user account.
 *
 * <p>Created by {@link UserFactory} when the type {@code "platinum"} is requested.
 * Inherits all properties and behaviour from {@link User}.</p>
 */
public class PlatinumUser extends User {

    /**
     * Constructs a new PlatinumUser with the given properties.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the age
     */
    public PlatinumUser(String username, String email, String password, int age) {
        // Delegate to the parent constructor, which routes through setters
        super(username, email, password, age);
    }
}
