package com.bilvantis.ecommerce.api.util;

public class UserConstants {

    public static final String PHONE_NUMBER_EXISTS = "A user with the phone number %s already exists.";
    public static final String EMAIL_EXISTS = "A user with the email %s already exists.";

    public static final String USER_SAVE_FAILED = "User save failed.";
    public static final String USER_NOT_FOUND = "User not found with id: %s";
    public static final String USER_FETCH_FAILED = "Error fetching user by id.";
    public static final String USERS_FETCH_FAILED = "Error fetching users list.";
    public static final String NO_USERS_FOUND = "No users found.";
    public static final String ERROR_UPDATING_USER = "Error updating user with id: %s";
    public static final String ERROR_DELETING_USER = "Error deleting user with id: %s";
    public static final String USER_ID_MISMATCH = "User ID mismatch: provided ID %s does not match DTO ID %s";
    public static final String REGISTRATION_SUCCESS_EMAIL_SUBJECT = "Registration Successful - OTP Verification";

}
