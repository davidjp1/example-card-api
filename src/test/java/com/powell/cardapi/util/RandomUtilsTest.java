package com.powell.cardapi.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUtilsTest {

    @Test
    public void generatesAUUID(){
        assertThat(RandomUtils.randomUUID()).matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$");
    }
}