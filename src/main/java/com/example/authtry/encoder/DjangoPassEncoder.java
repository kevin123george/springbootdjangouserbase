package com.example.authtry.encoder;


import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class DjangoPassEncoder implements PasswordEncoder {
    public static final String SALT = "3bP7pOYHmLza";
    private Hasher hasher;

    public DjangoPassEncoder() {
        hasher = new Hasher();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return hasher.encode(rawPassword.toString(),SALT);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return hasher.checkPassword(rawPassword.toString(), encodedPassword);
    }


    private String getSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[18];
        random.nextBytes(salt);
        return salt.toString();
    }
}