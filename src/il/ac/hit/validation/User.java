package il.ac.hit.validation;

/**
 * Represents a user in the validation system.
 *
 * <p>Holds the four core properties of a user account: username, email,
 * password, and age. All subclasses ({@link BasicUser}, {@link PremiumUser},
 * {@link PlatinumUser}) are created through {@link UserFactory}.</p>
 */
public class User {

    /** The unique login name of the user. */
    private String username;

    /** The email address of the user. */
    private String email;

    /** The password of the user. */
    private String password;

    /** The age of the user. */
    private int age;

    /**
     * Constructs a new User with the given properties.
     *
     * <p>Delegates every assignment to the corresponding setter so that
     * any validation logic added to a setter in the future is respected
     * even during construction.</p>
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the age
     */
    public User(String username, String email, String password, int age) {
        // Route through setters — avoids direct field assignment
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setAge(age);
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns a human-readable representation of this user.
     *
     * @return a string containing the username, email, and age
     */
    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
