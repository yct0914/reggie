package com.marconi.rice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Marconi
 * @date 2022/7/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.marconi.common")
public class CommonProperties {

    /**
     *需要上传的图片保存目录
     */
    private String imageDirectory;
}
