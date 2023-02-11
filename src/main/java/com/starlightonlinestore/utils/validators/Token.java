package com.starlightonlinestore.utils.validators;

import java.security.SecureRandom;

public class Token {
    private static SecureRandom random = new SecureRandom();

//    public static void main(String[] args) {
//        generateToken(4);
//    }

//    public static String generateToken() {
        public static String generateToken(int len) {
            StringBuilder randomNumber = new StringBuilder(len);
            for(int i = 0; i < len; i++){
                String combineWord = "0123456789";
                randomNumber.append(combineWord.charAt(random.nextInt(combineWord.length())));
            }
            return new String(randomNumber);
        }

//        SecureRandom secureRandom = new SecureRandom();
//        int number = secureRandom.nextInt(0, 9);
//        int num = secureRandom.nextInt(0, 9);
//        int numb = secureRandom.nextInt(0, 9);
//        int nums = secureRandom.nextInt(0, 9);
//        return String.format("%d%d%d%d", number, num, nums, numb);

}
