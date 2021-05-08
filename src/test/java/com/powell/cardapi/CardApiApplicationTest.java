package com.powell.cardapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class CardApiApplicationTest {

    // For coverage purposes only, does not test Implementation
    // Testing the entire app's context starts would be time consuming for build time.
    @Test
    @SuppressWarnings("squid:S2699")
    public void main() {
        PowerMockito.mockStatic(SpringApplication.class);
        String[] args = {"Hello"};
        CardApiApplication.main(args);
        PowerMockito.verifyStatic(SpringApplication.class);
        SpringApplication.run(CardApiApplication.class, args);
    }

}