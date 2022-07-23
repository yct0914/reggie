package com.marconi.reggie.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Marconi
 * @date 2022/7/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.marconi.redis")
public class RedisProperties {

    /**
     * redis默认缓存过期时间
     */
    private Long ttlMillis = 1000 * 60 * 60 * 24L;
}
