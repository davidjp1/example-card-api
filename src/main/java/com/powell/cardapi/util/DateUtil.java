package com.powell.cardapi.util;

import lombok.Setter;

import java.time.Clock;
import java.time.LocalDate;

public class DateUtil {
    @Setter
    private static Clock clock = Clock.systemUTC();

    public static LocalDate dateNowUTC(){
        return LocalDate.now(clock);
    }
}
