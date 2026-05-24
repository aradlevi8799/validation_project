package il.ac.hit.validation;

/**
 * Creates {@link User} objects based on a requested tier type.
 *
 * <p><b>Design pattern — Factory Method:</b> the static {@link #create} method is
 * the factory method. Instead of the caller constructing a concrete subclass
 * directly (e.g. {@code new BasicUser(...)}), it asks the factory for a {@link User}
 * by name. The factory decides which concrete class to instantiate, so the caller
 * stays decoupled from the subclass hierarchy.</p>
 *
 * <p>Supported types: {@code "basic"}, {@code "premium"}, {@code "platinum"}.</p>
 */
public class UserFactory {

    /**
     * Creates and returns a {@link User} of the appropriate subtype.
     *
     * <p>This is the factory method: it reads the {@code type} string and
     * instantiates the matching concrete {@link User} subclass.</p>
     *
     * @param type     the tier type — {@code "basic"}, {@code "premium"}, or {@code "platinum"}
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the age
     * @return a new {@link User} instance of the appropriate subtype
     * @throws IllegalArgumentException if {@code type} is not one of the supported values
     */
    public static User create(String type, String username, String email,
                              String password, int age) {
        // Select the concrete subclass based on the requested tier
        switch (type.toLowerCase()) {
            case "basic":
                return new BasicUser(username, email, password, age);
            case "premium":
                return new PremiumUser(username, email, password, age);
            case "platinum":
                return new PlatinumUser(username, email, password, age);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
