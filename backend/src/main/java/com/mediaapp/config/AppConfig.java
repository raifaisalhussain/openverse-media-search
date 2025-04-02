package com.mediaapp.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@Configuration
public class AppConfig {

    /**
     * Insecurely bypass SSL checks by overriding Java's default SSL context.
     * DO NOT USE IN PRODUCTION.
     */
    @PostConstruct
    public void disableSslVerification() throws Exception {
        // 1) Create a naive TrustManager that trusts all certs
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // 2) Override default socket factory with our insecure context
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // 3) Bypass hostname checks
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    /**
     * Provide a simple RestTemplate.
     * It uses Java's default SSL, which we already disabled in disableSslVerification().
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
