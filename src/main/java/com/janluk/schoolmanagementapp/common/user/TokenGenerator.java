package com.janluk.schoolmanagementapp.common.user;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    public static final String CHARACTER_POOL = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    public static final String NUMBER_POOL = "0123456789";

    public static String generateToken() {
        return RandomStringUtils.random(25, CHARACTER_POOL + NUMBER_POOL);
    }
}
