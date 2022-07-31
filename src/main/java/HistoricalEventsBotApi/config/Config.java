package HistoricalEventsBotApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${bot.password}")
    private String adminPassword;

    @Value("${siteconfig.url}")
    private String url;

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getSiteUrl() {
        return url;
    }

}
