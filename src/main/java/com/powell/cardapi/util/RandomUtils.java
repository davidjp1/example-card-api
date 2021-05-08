package com.powell.cardapi.util;

import java.util.UUID;

public class RandomUtils {

    private RandomUtils() {}

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
