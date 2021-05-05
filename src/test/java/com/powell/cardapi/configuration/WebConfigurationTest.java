package com.powell.cardapi.configuration;

import org.junit.Test;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebConfigurationTest {

    private WebConfiguration sut = new WebConfiguration();

    @Test
    public void addsRedirect() {
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        sut.addViewControllers(registry);
        verify(registry).addRedirectViewController("/", "/swagger-ui.html");
    }

}