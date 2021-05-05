package com.powell.cardapi.configuration;

import org.junit.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigurationTest {

    @Test
    public void swaggerConfiguration_createsDocket() {
        new ApplicationContextRunner()
                .withUserConfiguration(SwaggerConfiguration.class).run(ctx ->
                assertThat(ctx.getBean(Docket.class)).isNotNull());
    }
}