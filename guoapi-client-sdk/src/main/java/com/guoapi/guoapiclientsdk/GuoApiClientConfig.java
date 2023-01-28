package com.guoapi.guoapiclientsdk;

import com.guoapi.guoapiclientsdk.client.GuoApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author rivenzhou
 */
@Configuration
@ConfigurationProperties("guoapi.client")
@Data
@ComponentScan
public class GuoApiClientConfig {
    private String accessKey;

    private String secretKey;

    @Bean
    public GuoApiClient guoApiClient() {
        return new GuoApiClient(accessKey, secretKey);
    }
}
