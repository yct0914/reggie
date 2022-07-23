package com.marconi.reggie.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Marconi
 * @date 2022/7/16
 */

@Component
@ConfigurationProperties(prefix = "com.marconi.jwt")
public class JwtProperties {

    /**
     * JWT默认有效期
     */
    public static Long ttlMillis =  60 * 60 * 1000L;

    /**
     * 秘钥明文
     */
    public static String jwtKey = "Marconi";

    public static Long getTtlMillis() {
        return ttlMillis;
    }

    public void setTtlMillis(Long ttlMillis) {
        JwtProperties.ttlMillis = ttlMillis;
    }

    public static String getJwtKey() {
        return jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        JwtProperties.jwtKey = jwtKey;
    }
}
