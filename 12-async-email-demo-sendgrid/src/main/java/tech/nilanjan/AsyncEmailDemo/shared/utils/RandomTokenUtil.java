package tech.nilanjan.AsyncEmailDemo.shared.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomTokenUtil {
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random RANDOM = new Random();
    private final Long LENGTH = 100L;

    public String generateEmailVerificationToken() {
        return generateRandomToken();
    }

    private String generateRandomToken() {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            returnValue.append(
                    CHARACTERS.charAt(
                            RANDOM.nextInt(0, CHARACTERS.length())
                    )
            );
        }
        return new String(returnValue);
    }

}
