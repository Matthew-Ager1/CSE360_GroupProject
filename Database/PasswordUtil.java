package Database;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hashes a password using bcrypt
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 is the log rounds
    }

    // Verifies a password against a hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hashed password format.");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}