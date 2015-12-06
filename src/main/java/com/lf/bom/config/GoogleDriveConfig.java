package com.lf.bom.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class GoogleDriveConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveConfig.class);

    @Value("${google.api.application.name}")
    private String applicationName;

    @Value("${google.api.credential.dir}")
    private String credentialDir;

    @Value("${google.api.auth.token.path}")
    private String authTokenPath;

    private JsonFactory jsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    private FileDataStoreFactory fileDataStoreFactory() throws IOException {
        return new FileDataStoreFactory(credentialDir());
    }

    private File credentialDir() {
        return new java.io.File(
                System.getProperty("user.home"), credentialDir + applicationName);
    }

    private HttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    private Credential authorize() throws IOException, GeneralSecurityException {

        // Load client secrets.
        InputStream in = new FileInputStream(System.getProperty("user.home") + authTokenPath);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory(), new InputStreamReader(in));

        List<String> scopes = new ArrayList<>();
        scopes.addAll(DriveScopes.all());

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport(), jsonFactory(), clientSecrets, scopes)
                        .setDataStoreFactory(fileDataStoreFactory())
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        LOGGER.info("Credentials saved to " + credentialDir().getAbsolutePath());
        return credential;
    }

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Drive.Builder(
                httpTransport(), jsonFactory(), credential)
                .setApplicationName(applicationName)
                .build();
    }

}
