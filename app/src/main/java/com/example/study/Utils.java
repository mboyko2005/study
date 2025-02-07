package com.example.study;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Utils {

    /**
     * Хеширует пароль с использованием алгоритма SHA-256.
     *
     * @param password исходный пароль
     * @return хешированный пароль в виде шестнадцатеричной строки
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            // В норме алгоритм SHA-256 всегда присутствует
            throw new RuntimeException("Алгоритм хеширования не найден!", e);
        }
    }

    /**
     * Преобразует массив байт в шестнадцатеричное представление.
     *
     * @param hash массив байт
     * @return строка в шестнадцатеричном формате
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
