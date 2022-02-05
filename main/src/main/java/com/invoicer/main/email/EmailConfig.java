package com.invoicer.main.email;

public class EmailConfig {

    private boolean smtpAuth;
    private boolean smtpStartTtsEnable;
    private String host;
    private int port;
    private String sslTrust;
    private String username;
    private String password;

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public boolean isSmtpStartTtsEnable() {
        return smtpStartTtsEnable;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSslTrust() {
        return sslTrust;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public void setSmtpStartTtsEnable(boolean smtpStartTtsEnable) {
        this.smtpStartTtsEnable = smtpStartTtsEnable;
    }

    public void setSslTrust(String sslTrust) {
        this.sslTrust = sslTrust;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
