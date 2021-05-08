package com.powell.cardapi.util;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilTest {

    @Test
    public void dateNow(){
        assertThat(DateUtil.dateNowUTC()).isNotNull();
    }

}