package com.powell.cardapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperConfigurationTest {

    @Test
    public void createsObjectMapper(){
        new ApplicationContextRunner().withUserConfiguration(ObjectMapperConfiguration.class)
                .run(ctx -> assertThat(ctx.getBean(ObjectMapper.class)).isNotNull());
    }

}