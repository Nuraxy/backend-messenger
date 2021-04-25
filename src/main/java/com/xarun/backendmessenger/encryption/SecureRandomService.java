package com.xarun.backendmessenger.encryption;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SecureRandomService {

    public byte[] getBytes() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
