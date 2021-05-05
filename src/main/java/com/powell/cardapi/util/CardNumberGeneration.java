package com.powell.cardapi.util;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CardNumberGeneration {

    private CardNumberGeneration() { }

    public static String generateCardNumber() {
        return randomIntString(15);
    }

    public static String generateCVV() {
        return randomIntString(3);
    }

    private static String randomIntString(int length){

        return ThreadLocalRandom.current().ints(length, 0, 9).boxed()
                .map(Objects::toString)
                .collect(Collectors.joining());
    }


}
