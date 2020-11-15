package com.example.timeserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("featureswitch")
public class FeatureSwitchConfiguration {
    private boolean printIp;
    private boolean emailUp;
}
