package com.powell.cardapi.util;

import org.junit.Test;

import static com.powell.cardapi.util.CardNumberGeneration.generateCVV;
import static com.powell.cardapi.util.CardNumberGeneration.generateCardNumber;
import static java.util.regex.Pattern.compile;
import static org.assertj.core.api.Assertions.assertThat;

public class CardNumberGenerationTest {

    @Test
    public void generateCardNumber_generatesValidNumber() {
        assertThat(generateCardNumber()).matches(compile("^[0-9]{15}$"));
    }

    @Test
    public void generateCVV_generatesValidNumber() {
        assertThat(generateCVV()).matches(compile("^[0-9]{3}$"));
    }
}
