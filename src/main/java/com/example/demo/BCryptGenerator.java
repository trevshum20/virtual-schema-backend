package com.example.demo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BCryptGenerator {
    public static void main(String[] args) {
        // Replace "secret" with the plaintext password you want to hash
        String rawPassword = "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("BCrypted: " + encodedPassword);
    }
}
