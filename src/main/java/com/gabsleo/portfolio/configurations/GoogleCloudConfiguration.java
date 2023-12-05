package com.gabsleo.portfolio.configurations;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class GoogleCloudConfiguration {
    @Value("${env.GCP_CLIENT_ID}")
    private String clientId;

    @Value("${env.GCP_SERVICE_ACCOUNT}")
    private String serviceAccount;

    @Value("${env.GCP_PRIVATE_KEY}")
    private String privateKey;

    @Value("${env.GCP_PRIVATE_KEY_ID}")
    private String privateKeyId;

    @Bean
    public Storage gcloudStorage() throws FileNotFoundException, IOException {
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromPkcs8(
                        clientId,
                        serviceAccount,
                        privateKey.replace("\\n", "\n"),
                        privateKeyId,
                        null
                ))
                .build()
                .getService();
    }
}
