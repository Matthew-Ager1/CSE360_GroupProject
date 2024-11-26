package Database;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password) {
        // Generate a salt and hash the password with bcrypt
        String salt = BCrypt.gensalt(12); // 12 is the log_rounds
        return BCrypt.hashpw(password, salt);
    }

    public static boolean verifyPassword(String enteredPassword, String storedHashedPassword) {
        // Verify the entered password against the stored bcrypt hash
        return BCrypt.checkpw(storedHashedPassword, storedHashedPassword);
    }
}

