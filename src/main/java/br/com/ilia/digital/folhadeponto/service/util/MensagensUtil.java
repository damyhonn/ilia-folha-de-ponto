package br.com.ilia.digital.folhadeponto.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:messages.properties")
public class MensagensUtil {

    @Autowired
    private Environment env;

    public String getProperty(String prop) {
        return env.getProperty(prop);
    }
} 
