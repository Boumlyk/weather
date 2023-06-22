package com.test.metio.tools.security;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashManager {

    public static final int Base64_FORM = 1;
    public static final int HEX_FORM = 2;

    private static final int ITERATIONS = 3;


    public static String hashString(String data, int form) {
        if (Base64_FORM == form)
            return Base64.encodeToString(hashString(data, getAppropriateHash()), Base64.NO_WRAP);
        return toHex(hashString(data, getAppropriateHash()));
    }

    public static String hashString(String data, int form, HashMethod method) {
        if (Base64_FORM == form)
            return Base64.encodeToString(hashString(data, method), Base64.NO_WRAP);
        return toHex(hashString(data, method));
    }

    public static byte[] hashString(String data, HashMethod method) {
        if (HashMethod.PBKDF2.getHashString().equals(method.getHashString())) {
            return hashBasedOnPBKDF(data);
        } else {
            for (int i = 0; i < ITERATIONS; i++)
                return hashBasedOnDigest(data, method.getHashString());
        }
        return data.getBytes();
    }

    private static byte[] hashBasedOnPBKDF(String data) {
        char[] chars = new char[data.length()];
        data.getChars(0, data.length(), chars, 0);
        byte[] salt = "salt_on_client_is_funny".getBytes();
        byte[] hashedPassBytes = new byte[0];
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HashMethod.PBKDF2.getHashString());
            KeySpec keySpec = new PBEKeySpec(chars, salt, ITERATIONS, 512);
            hashedPassBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (Exception ignored) {
        }
        return hashedPassBytes;
    }

    private static byte[] hashBasedOnDigest(String data, String algorithm) {
        byte[] digest = new byte[0];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(data.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return digest;
    }

    public static HashMethod getAppropriateHash() {
        HashMethod method = null;

        if (isPBKDFAvailable()) {
            method = HashMethod.PBKDF2;
        } else if (isDigestAvailable(HashMethod.SHA512.getHashString())) {
            method = HashMethod.SHA512;
        } else if (isDigestAvailable(HashMethod.SHA384.getHashString())) {
            method = HashMethod.SHA384;
        } else if (isDigestAvailable(HashMethod.SHA256.getHashString())) {
            method = HashMethod.SHA256;
        } else if (isDigestAvailable(HashMethod.SHA1.getHashString())) {
            method = HashMethod.SHA1;
        }
        return method;
    }

    private static boolean isPBKDFAvailable() {
        try {
            SecretKeyFactory.getInstance(HashMethod.PBKDF2.getHashString());
        } catch (Exception notAvailable) {
            return false;
        }
        return true;
    }

    private static boolean isDigestAvailable(String method) {
        try {
            MessageDigest.getInstance(method);
        } catch (Exception notAvailable) {
            return false;
        }

        return true;
    }

    public enum HashMethod {
        PBKDF2() {
            @Override
            public String getHashString() {
                return "PBKDF2WithHmacSHA1";
            }
        }, SHA512() {
            @Override
            public String getHashString() {
                return "SHA-512";
            }
        }, SHA384() {
            @Override
            public String getHashString() {
                return "SHA-384";
            }
        }, SHA256() {
            @Override
            public String getHashString() {
                return "SHA-256";
            }
        }, SHA1() {
            @Override
            public String getHashString() {
                return "SHA-1";
            }
        };

        public abstract String getHashString();

    }


    public static String toHex(byte[] data) {
        char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte datum : data) {
            sb.append(DIGITS[(datum >>> 4) & 0x0F]);
            sb.append(DIGITS[datum & 0x0F]);
        }
        return sb.toString();
    }


}