package il.ac.hit.validation;

/**
 * Represents a basic-tier user account.
 *
 * <p>Created by {@link UserFactory} when the type {@code "basic"} is requested.
 * Inherits all properties and behaviour from {@link User}.</p>
 */
public class BasicUser extends User {

    /**
     * Constructs a new BasicUser with the given properties.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the age
     */
    public BasicUser(String username, String email, String password, int age) {
        // Delegate to the parent constructor, which routes through setters
        super(username, email, password, age);
    }
}
