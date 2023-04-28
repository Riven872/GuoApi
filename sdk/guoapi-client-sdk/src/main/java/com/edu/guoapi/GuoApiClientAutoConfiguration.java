package com.edu.guoapi;

import com.edu.guoapi.client.GuoApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
// @ComponentScan
@ConfigurationProperties("guoapi.client")
public class GuoApiClientAutoConfiguration {
    /**
     * 用户授权名
     */
    private String accessKey;

    /**
     * 用户授权密钥
     */
    private String secretKey;

    /**
     * 手动指定网关地址
     */
    private String gatewayAddr;

    @Bean
    public GuoApiClient guoApiClient() {
        return new GuoApiClient(accessKey, secretKey, gatewayAddr);
    }
}
