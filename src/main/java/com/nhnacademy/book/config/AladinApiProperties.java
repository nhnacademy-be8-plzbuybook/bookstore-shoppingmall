package com.nhnacademy.book.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aladin.api.lookup")
public class AladinApiProperties {
    private String baseUrl;
    private String ttbkey;
    private String output;
    private String version;

    // Getter & Setter
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getTtbkey() { return ttbkey; }
    public void setTtbkey(String ttbkey) { this.ttbkey = ttbkey; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}
