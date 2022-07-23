package com.marconi.reggie.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11482
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.marconi.security")
public class SecurityProperties {

    /**
     * spring-security无需权限即可访问paths
     */
    private List<String> permitPaths = new ArrayList<>(16);
}
