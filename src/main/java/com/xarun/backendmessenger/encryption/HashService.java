package com.xarun.backendmessenger.encryption;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class HashService {

    public String hashPassword(byte[] salt, String plainText) {
        final MessageDigest digest;
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        if (salt != null) {
            byte[] allByteArray = new byte[plainTextBytes.length + salt.length];
            ByteBuffer buff = ByteBuffer.wrap(allByteArray);
            buff.put(plainTextBytes);
            buff.put(salt);
            byte[] combined = buff.array();
            try {
                digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(combined);
                return Base64.getEncoder().encodeToString(encodedhash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            byte[] allByteArray = new byte[plainTextBytes.length];
            ByteBuffer buff = ByteBuffer.wrap(allByteArray);
            buff.put(plainTextBytes);
            byte[] combined = buff.array();
            try {
                digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(combined);
                return Base64.getEncoder().encodeToString(encodedhash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String plainHash(String plainText) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
