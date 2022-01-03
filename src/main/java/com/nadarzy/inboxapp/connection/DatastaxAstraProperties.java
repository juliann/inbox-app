package com.nadarzy.inboxapp.connection;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * @author Julian Nadarzy on 03/01/2022
 */
@ConfigurationProperties(prefix = "datastax.astra")
public class DatastaxAstraProperties {

    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }
}
