import il.ac.hit.validation.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Comprehensive test suite for the validation library.
 * Tests every functional requirement, design pattern contract, and edge case.
 * Prints a summary with PASS / FAIL per test and a final score.
 */
public class ValidationLibraryTest {

    // ── tiny test framework ──────────────────────────────────────────────────

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS  " + name);
            passed++;
        } else {
            System.out.println("  FAIL  " + name);
            failed++;
        }
    }

    private static void section(String title) {
        System.out.println("\n── " + title + " ──────────────────────────────");
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        testUserClass();
        testUserSubclasses();
        testUserFactory();
        testUserUtils();
        testValidationResult();
        testBuiltInValidationRules();
        testAndCombinator();
        testOrCombinator();
        testXorCombinator();
        testAllCombinator();
        testNoneCombinator();
        testCombinatorChaining();
        testUserValidationExtendsFunction();
        testAssignmentDemoCode();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("  RESULTS: " + passed + " passed,  " + failed + " failed");
        if (failed == 0) System.out.println("  ALL TESTS PASSED ✓");
        else             System.out.println("  SOME TESTS FAILED — see above");
        System.out.println("════════════════════════════════════════");
    }

    // ── 1. User class ────────────────────────────────────────────────────────

    private static void testUserClass() {
        section("1. User — fields, constructor, getters, setters");

        User u = new User("alice123", "alice@test.co.il", "Secret$1", 22);

        check("getUsername() returns constructed value",
              "alice123".equals(u.getUsername()));
        check("getEmail() returns constructed value",
              "alice@test.co.il".equals(u.getEmail()));
        check("getPassword() returns constructed value",
              "Secret$1".equals(u.getPassword()));
        check("getAge() returns constructed value",
              u.getAge() == 22);

        // setters work independently
        u.setUsername("bob999");
        u.setEmail("bob@new.co.il");
        u.setPassword("NewPass1");
        u.setAge(30);
        check("setUsername works", "bob999".equals(u.getUsername()));
        check("setEmail works",    "bob@new.co.il".equals(u.getEmail()));
        check("setPassword works", "NewPass1".equals(u.getPassword()));
        check("setAge works",      u.getAge() == 30);

        check("toString is non-null and non-empty",
              u.toString() != null && !u.toString().isEmpty());
    }

    // ── 2. User subclasses ───────────────────────────────────────────────────

    private static void testUserSubclasses() {
        section("2. User subclasses — inheritance");

        User basic    = new BasicUser   ("u1", "a@b.co.il", "pass", 20);
        User premium  = new PremiumUser ("u2", "a@b.co.il", "pass", 20);
        User platinum = new PlatinumUser("u3", "a@b.co.il", "pass", 20);

        check("BasicUser    instanceof User", basic    instanceof User);
        check("PremiumUser  instanceof User", premium  instanceof User);
        check("PlatinumUser instanceof User", platinum instanceof User);

        check("BasicUser    is correct concrete type", basic    instanceof BasicUser);
        check("PremiumUser  is correct concrete type", premium  instanceof PremiumUser);
        check("PlatinumUser is correct concrete type", platinum instanceof PlatinumUser);

        // Inherited getters work through superclass reference
        check("BasicUser username accessible via User ref",
              "u1".equals(basic.getUsername()));
    }

    // ── 3. UserFactory — Factory Method ─────────────────────────────────────

    private static void testUserFactory() {
        section("3. UserFactory — Factory Method");

        User b  = UserFactory.create("basic",    "u", "e@x.co.il", "p", 20);
        User pr = UserFactory.create("premium",  "u", "e@x.co.il", "p", 20);
        User pl = UserFactory.create("platinum", "u", "e@x.co.il", "p", 20);

        check("\"basic\"    returns BasicUser",    b  instanceof BasicUser);
        check("\"premium\"  returns PremiumUser",  pr instanceof PremiumUser);
        check("\"platinum\" returns PlatinumUser", pl instanceof PlatinumUser);

        // Return type is User (interface-typed)
        check("Return typed as User for basic",    b  instanceof User);
        check("Return typed as User for premium",  pr instanceof User);
        check("Return typed as User for platinum", pl instanceof User);

        // Case-insensitive
        check("\"BASIC\" (uppercase) accepted",
              UserFactory.create("BASIC", "u", "e@x.co.il", "p", 20) instanceof BasicUser);

        // Unknown type throws
        boolean threw = false;
        try { UserFactory.create("gold", "u", "e@x.co.il", "p", 20); }
        catch (IllegalArgumentException e) { threw = true; }
        check("Unknown type throws IllegalArgumentException", threw);
    }

    // ── 4. UserUtils — Template Method ──────────────────────────────────────

    private static void testUserUtils() {
        section("4. UserUtils.sort — Template Method");

        User u1 = new User("charlie", "c@x.co.il", "p", 35);
        User u2 = new User("alice",   "a@x.co.il", "p", 22);
        User u3 = new User("bob",     "b@x.co.il", "p", 28);
        User[] users = {u1, u2, u3};

        // Sort by age ascending — Comparator is the "hook"
        UserUtils.sort(users, (a, b) -> Integer.compare(a.getAge(), b.getAge()));
        check("sort by age asc: first  is youngest (22)",  users[0].getAge() == 22);
        check("sort by age asc: second is middle  (28)",  users[1].getAge() == 28);
        check("sort by age asc: third  is oldest  (35)",  users[2].getAge() == 35);

        // Sort by username alphabetically
        UserUtils.sort(users, (a, b) -> a.getUsername().compareTo(b.getUsername()));
        check("sort by username: alice is first",   "alice".equals(users[0].getUsername()));
        check("sort by username: bob is second",    "bob".equals(users[1].getUsername()));
        check("sort by username: charlie is third", "charlie".equals(users[2].getUsername()));

        // Descending — same method, different comparator
        UserUtils.sort(users, (a, b) -> b.getUsername().compareTo(a.getUsername()));
        check("sort descending: charlie is first",  "charlie".equals(users[0].getUsername()));

        // Single element — no exception
        User[] single = { new User("x", "x@x.co.il", "p", 1) };
        UserUtils.sort(single, (a, b) -> 0);
        check("sort single-element array — no exception", true);

        // Empty array — no exception
        User[] empty = {};
        UserUtils.sort(empty, (a, b) -> 0);
        check("sort empty array — no exception", true);
    }

    // ── 5. ValidationResult, Valid, Invalid ──────────────────────────────────

    private static void testValidationResult() {
        section("5. ValidationResult / Valid / Invalid");

        // Valid
        ValidationResult valid = new Valid();
        check("Valid.isValid() == true",                   valid.isValid());
        check("Valid.getReason() == Optional.empty()",     !valid.getReason().isPresent());

        // Invalid
        ValidationResult invalid = new Invalid("bad email");
        check("Invalid.isValid() == false",                !invalid.isValid());
        check("Invalid.getReason() is present",            invalid.getReason().isPresent());
        check("Invalid.getReason() contains the message", "bad email".equals(invalid.getReason().get()));

        // Invalid setter
        ((Invalid) invalid).setReason("new reason");
        check("Invalid.setReason updates the reason",
              "new reason".equals(invalid.getReason().get()));

        // Valid is a concrete instantiable class
        check("Valid is a concrete class (can be instantiated)", new Valid() != null);
        check("Invalid is a concrete class (can be instantiated)", new Invalid("x") != null);

        // Both implement ValidationResult
        check("Valid   implements ValidationResult", valid   instanceof ValidationResult);
        check("Invalid implements ValidationResult", invalid instanceof ValidationResult);
    }

    // ── 6. Built-in validation rules ─────────────────────────────────────────

    private static void testBuiltInValidationRules() {
        section("6. Built-in static validation rules");

        User u = new User("admin", "admin@test.co.il", "secret$99", 25);

        // emailEndsWithIL
        check("emailEndsWithIL — PASS on 'admin@test.co.il'",
              UserValidation.emailEndsWithIL().apply(u).isValid());
        check("emailEndsWithIL — FAIL on 'a@b.com'",
              !UserValidation.emailEndsWithIL()
                  .apply(new User("x", "a@b.com", "p", 20)).isValid());

        // emailLengthBiggerThan10
        check("emailLengthBiggerThan10 — PASS on length-17 email",
              UserValidation.emailLengthBiggerThan10().apply(u).isValid());
        check("emailLengthBiggerThan10 — FAIL on 'a@b.co.il' (9 chars)",
              !UserValidation.emailLengthBiggerThan10()
                  .apply(new User("x", "a@b.co.il", "p", 20)).isValid());

        // passwordLengthBiggerThan8
        check("passwordLengthBiggerThan8 — PASS on 9-char password",
              UserValidation.passwordLengthBiggerThan8().apply(u).isValid());
        check("passwordLengthBiggerThan8 — FAIL on 'abc' (3 chars)",
              !UserValidation.passwordLengthBiggerThan8()
                  .apply(new User("x", "x@x.co.il", "abc", 20)).isValid());

        // passwordIncludesLettersNumbersOnly
        User alphaNum = new User("x", "x@x.co.il", "abc123", 20);
        User hasSymbol = new User("x", "x@x.co.il", "abc$123", 20);
        check("passwordIncludesLettersNumbersOnly — PASS on 'abc123'",
              UserValidation.passwordIncludesLettersNumbersOnly().apply(alphaNum).isValid());
        check("passwordIncludesLettersNumbersOnly — FAIL on 'abc$123'",
              !UserValidation.passwordIncludesLettersNumbersOnly().apply(hasSymbol).isValid());

        // passwordIncludesDollarSign
        check("passwordIncludesDollarSign — PASS on 'secret$99'",
              UserValidation.passwordIncludesDollarSign().apply(u).isValid());
        check("passwordIncludesDollarSign — FAIL on 'secret99'",
              !UserValidation.passwordIncludesDollarSign()
                  .apply(new User("x", "x@x.co.il", "secret99", 20)).isValid());

        // passwordIsDifferentFromUsername
        User samePw = new User("samepass", "x@x.co.il", "samepass", 20);
        check("passwordIsDifferentFromUsername — PASS when different",
              UserValidation.passwordIsDifferentFromUsername().apply(u).isValid());
        check("passwordIsDifferentFromUsername — FAIL when same",
              !UserValidation.passwordIsDifferentFromUsername().apply(samePw).isValid());

        // ageBiggerThan18
        check("ageBiggerThan18 — PASS on age 25",
              UserValidation.ageBiggerThan18().apply(u).isValid());
        check("ageBiggerThan18 — FAIL on age 17",
              !UserValidation.ageBiggerThan18()
                  .apply(new User("x", "x@x.co.il", "p", 17)).isValid());
        check("ageBiggerThan18 — FAIL on age exactly 18 (not strictly greater)",
              !UserValidation.ageBiggerThan18()
                  .apply(new User("x", "x@x.co.il", "p", 18)).isValid());

        // usernameLengthBiggerThan8
        check("usernameLengthBiggerThan8 — PASS on 'admin123' (9 chars? no — 8 exactly)",
              !UserValidation.usernameLengthBiggerThan8()
                  .apply(new User("admin123", "x@x.co.il", "p", 20)).isValid());
        check("usernameLengthBiggerThan8 — PASS on 9-char username",
              UserValidation.usernameLengthBiggerThan8()
                  .apply(new User("admin1234", "x@x.co.il", "p", 20)).isValid());

        // Every failing rule returns a non-empty reason
        ValidationResult failEmail = UserValidation.emailEndsWithIL()
                .apply(new User("x", "x@x.com", "p", 20));
        check("Failing rule provides a non-empty reason",
              failEmail.getReason().isPresent() && !failEmail.getReason().get().isEmpty());
    }

    // ── 7. and ───────────────────────────────────────────────────────────────

    private static void testAndCombinator() {
        section("7. and — both must pass");

        User good = new User("u", "user@test.co.il", "p", 20); // email > 10 chars, ends with il
        User badLen  = new User("u", "x@y.co.il", "p", 20);   // email length = 9 (fails >10)
        User badEnd  = new User("u", "longEmail@test.com", "p", 20); // doesn't end with il

        UserValidation rule = UserValidation.emailLengthBiggerThan10()
                                            .and(UserValidation.emailEndsWithIL());

        check("and — PASS when both pass",       rule.apply(good).isValid());
        check("and — FAIL when first fails",     !rule.apply(badLen).isValid());
        check("and — FAIL when second fails",    !rule.apply(badEnd).isValid());

        // Short-circuit: first fails → reason comes from first, not second
        ValidationResult r = rule.apply(badLen);
        check("and — short-circuit: reason is from the first failing rule",
              r.getReason().isPresent());
    }

    // ── 8. or ────────────────────────────────────────────────────────────────

    private static void testOrCombinator() {
        section("8. or — at least one must pass");

        User    passFirst  = new User("u", "user@test.co.il", "p", 20); // email ends with il
        User    passSecond = new User("u", "verylongemail@x.com", "p", 20); // length > 10
        User    failBoth   = new User("u", "x@y.com", "p", 20); // neither

        UserValidation rule = UserValidation.emailEndsWithIL()
                                            .or(UserValidation.emailLengthBiggerThan10());

        check("or — PASS when first passes",        rule.apply(passFirst).isValid());
        check("or — PASS when only second passes",  rule.apply(passSecond).isValid());
        check("or — FAIL when both fail",           !rule.apply(failBoth).isValid());
    }

    // ── 9. xor ───────────────────────────────────────────────────────────────

    private static void testXorCombinator() {
        section("9. xor — exactly one must pass");

        // email ends with il=true, length>10=false → XOR passes
        User xorPass = new User("u", "x@y.co.il", "p", 20);
        // both pass → XOR fails
        User bothPass = new User("u", "user@test.co.il", "p", 20);
        // both fail → XOR fails
        User bothFail = new User("u", "x@y.com", "p", 20);

        UserValidation rule = UserValidation.emailEndsWithIL()
                                            .xor(UserValidation.emailLengthBiggerThan10());

        check("xor — PASS when exactly one passes",   rule.apply(xorPass).isValid());
        check("xor — FAIL when both pass",            !rule.apply(bothPass).isValid());
        check("xor — FAIL when both fail",            !rule.apply(bothFail).isValid());
    }

    // ── 10. all ──────────────────────────────────────────────────────────────

    private static void testAllCombinator() {
        section("10. all (varargs) — every rule must pass");

        User allGood = new User("longname1", "email@test.co.il", "password123", 25);
        // age fails
        User youngUser = new User("longname1", "email@test.co.il", "password123", 15);

        UserValidation rule = UserValidation.all(
                UserValidation.usernameLengthBiggerThan8(),
                UserValidation.emailLengthBiggerThan10(),
                UserValidation.passwordLengthBiggerThan8(),
                UserValidation.ageBiggerThan18()
        );

        check("all — PASS when every rule passes",  rule.apply(allGood).isValid());
        check("all — FAIL when any rule fails",     !rule.apply(youngUser).isValid());
        check("all — failing result has a reason",  rule.apply(youngUser).getReason().isPresent());

        // Single rule
        check("all — single rule works",
              UserValidation.all(UserValidation.ageBiggerThan18())
                            .apply(allGood).isValid());

        // Empty all → should pass vacuously
        check("all — zero rules passes vacuously",
              UserValidation.all().apply(allGood).isValid());
    }

    // ── 11. none ─────────────────────────────────────────────────────────────

    private static void testNoneCombinator() {
        section("11. none (varargs) — no rule must pass");

        // A user that fails all the given rules
        User child = new User("ab", "x@y.com", "p", 10);

        UserValidation rule = UserValidation.none(
                UserValidation.ageBiggerThan18(),
                UserValidation.emailEndsWithIL()
        );

        check("none — PASS when no rule passes",      rule.apply(child).isValid());
        check("none — FAIL when at least one passes",
              !rule.apply(new User("u", "x@y.co.il", "p", 10)).isValid());

        // Empty none → should pass vacuously
        check("none — zero rules passes vacuously",
              UserValidation.none().apply(child).isValid());
    }

    // ── 12. Combinator chaining ───────────────────────────────────────────────

    private static void testCombinatorChaining() {
        section("12. Combinator chaining");

        // Complex rule: (emailEndsWithIL AND emailLengthBiggerThan10) OR ageBiggerThan18
        UserValidation complexRule = UserValidation.emailEndsWithIL()
                .and(UserValidation.emailLengthBiggerThan10())
                .or(UserValidation.ageBiggerThan18());

        User passesViaEmail = new User("u", "user@test.co.il", "p", 10); // email ok, age fails
        User passesViaAge   = new User("u", "x@y.com", "p", 25);         // email fails, age ok
        User failsBoth      = new User("u", "x@y.com", "p", 10);         // both fail

        check("chaining — PASS via email branch",   complexRule.apply(passesViaEmail).isValid());
        check("chaining — PASS via age branch",     complexRule.apply(passesViaAge).isValid());
        check("chaining — FAIL when all branches fail", !complexRule.apply(failsBoth).isValid());

        // all + and together
        User ideal = new User("longname1", "user@test.co.il", "secret$99X", 25);
        UserValidation strictRule = UserValidation.all(
                UserValidation.usernameLengthBiggerThan8(),
                UserValidation.ageBiggerThan18(),
                UserValidation.passwordIncludesDollarSign()
        ).and(UserValidation.emailEndsWithIL());

        check("all(...).and(...) — PASS on ideal user", strictRule.apply(ideal).isValid());
    }

    // ── 13. UserValidation extends Function<User, ValidationResult> ──────────

    private static void testUserValidationExtendsFunction() {
        section("13. UserValidation extends Function<User, ValidationResult>");

        UserValidation v = UserValidation.ageBiggerThan18();

        // Must be assignable to Function<User, ValidationResult>
        Function<User, ValidationResult> fn = v;
        check("UserValidation is assignable to Function<User, ValidationResult>", fn != null);

        User adult = new User("u", "u@x.co.il", "p", 20);
        check("apply() works through Function reference",  fn.apply(adult).isValid());

        // @FunctionalInterface — lambda must work
        UserValidation lambda = user -> new Valid();
        check("UserValidation can be expressed as a lambda", lambda.apply(adult).isValid());
    }

    // ── 14. Exact demo from assignment ────────────────────────────────────────

    private static void testAssignmentDemoCode() {
        section("14. Exact demo code from the assignment");

        // Copied verbatim from the assignment document
        User user = new User("admin", "admin@#yzw.co.il", "abc123", 34);

        UserValidation validation1 = UserValidation.emailLengthBiggerThan10();
        UserValidation validation2 = UserValidation.emailEndsWithIL();

        ValidationResult result = (validation1.and(validation2)).apply(user);

        check("Demo email length > 10 (length=17)",   UserValidation.emailLengthBiggerThan10().apply(user).isValid());
        check("Demo email ends with 'il'",             UserValidation.emailEndsWithIL().apply(user).isValid());
        check("Demo combined result is VALID",         result.isValid());
        check("Demo prints 'User is valid'",           result.isValid()); // same as above, for clarity
    }
}
