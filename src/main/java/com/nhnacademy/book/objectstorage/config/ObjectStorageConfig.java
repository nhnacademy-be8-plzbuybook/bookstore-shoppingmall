package com.nhnacademy.book.objectstorage.config;

import com.nhnacademy.book.skm.properties.SKMProperties;
import com.nhnacademy.book.skm.service.SecureKeyManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectStorageConfig {

    private final SecureKeyManagerService secureKeyManagerService;
    private final SKMProperties skmProperties;

    public ObjectStorageConfig(SecureKeyManagerService secureKeyManagerService, SKMProperties skmProperties) {
        this.secureKeyManagerService = secureKeyManagerService;
        this.skmProperties = skmProperties;
    }

    @Bean
    public String getAuthUrl() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getAuthUrl());
    }

    @Bean
    public String getStorageUrl() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getUrl());
    }

    @Bean
    public String getContainerName() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getContainerName());
    }

    @Bean
    public String getTenantId() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getTenantId());
    }

    @Bean
    public String getUserName() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getUsername());
    }

    @Bean
    public String getPassword() {
        return secureKeyManagerService.fetchSecret(skmProperties.getObjectStorage().getPassword());
    }

}
