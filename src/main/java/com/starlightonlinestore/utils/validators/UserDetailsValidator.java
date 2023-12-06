package com.starlightonlinestore.utils.validators;

public class UserDetailsValidator {
    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 11;
    }

    public static boolean isValidEmailAddress(String email) {
        return email.contains("@");
    }
}
