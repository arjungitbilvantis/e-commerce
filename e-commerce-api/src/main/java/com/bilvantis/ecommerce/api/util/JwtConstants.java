package com.bilvantis.ecommerce.api.util;

public class JwtConstants {
    public static final String CLAIM_USER_ID = "user_id";
    public static final String CLAIM_PHONE_NUMBER = "phone_number";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_FIRST_NAME = "first_name";
    public static final String CLAIM_LAST_NAME = "last_name";
    public static final long EXPIRATION_TIME_MILLIS = 3600000; // 1 hour
    public static final String SECRET = "4027d080f9a3954189ebca5ddbbfdfc9cb824314c0d6c910eff8bec1c689eabc";
}
