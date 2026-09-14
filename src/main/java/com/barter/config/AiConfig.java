package com.barter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI 配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    /** 是否启用真实AI */
    private boolean enable = false;
}
