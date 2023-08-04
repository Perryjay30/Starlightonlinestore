package com.starlightonlinestore.utils.validators;

import java.security.SecureRandom;

public class Token {
    private static SecureRandom random = new SecureRandom();

        public static String generateToken(int len) {
            StringBuilder randomNumber = new StringBuilder(len);
            for(int i = 0; i < len; i++){
                String combineWord = "0123456789";
                randomNumber.append(combineWord.charAt(random.nextInt(combineWord.length())));
            }
            return new String(randomNumber);
        }

}
