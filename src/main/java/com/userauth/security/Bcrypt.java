package com.userauth.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Bcrypt {
    public static String hashpw(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    };

    public static boolean checkpw(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    };

    public static String gensalt() {
        return BCrypt.gensalt();
    };
 
}
