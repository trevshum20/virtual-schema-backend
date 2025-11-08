package com.example.demo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BCryptGenerator {
    public static void main(String[] args) {
        String rawPassword = (args.length > 0) ? args[0] : "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("BCrypted: " + encodedPassword);
    }
}
