package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomId {
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random RANDOM = new Random();

    public String generateUserId(int length) {
        return generateRandomId(length);
    }

    private String generateRandomId(int length) {
        StringBuilder returnValue = new StringBuilder();

        for (int i = 0; i < length; i++) {
            returnValue.append(CHARACTERS.charAt(RANDOM.nextInt(0, CHARACTERS.length())));
        }

        return new String(returnValue);
    }
}
