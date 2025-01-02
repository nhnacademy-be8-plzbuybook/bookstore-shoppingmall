package com.nhnacademy.book.skm.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.skm")
public class SKMProperties {
    private String url;
    private String appKey;
    private String keystoreFile;
    private String password;
    private Database Database;
    private Object_storage objectStorage;
    private Order_redis orderRedis;
    private Cart_redis cartRedis;

    @Getter
    @Setter
    public static class Database {
        private String url;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class Object_storage {
        private String authUrl;
        private String username;
        private String password;
        private String url;
        private String tenantId;
        private String containerName;
    }

    @Getter
    @Setter
    public static class Order_redis {
        private String host;
        private String port;
        private String password;
        private String range;
    }

    @Getter
    @Setter
    public static class Cart_redis {
        private String host;
        private String port;
        private String password;
        private String range;
    }
}

