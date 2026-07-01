package com.psmsf.lujanapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mercadopago")
public class MercadoPagoProperties {

    private String accessToken;
    private String backUrlSuccess;
    private String backUrlFailure;
    private String backUrlPending;
    private String webhookSecret;

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getBackUrlSuccess() { return backUrlSuccess; }
    public void setBackUrlSuccess(String backUrlSuccess) { this.backUrlSuccess = backUrlSuccess; }

    public String getBackUrlFailure() { return backUrlFailure; }
    public void setBackUrlFailure(String backUrlFailure) { this.backUrlFailure = backUrlFailure; }

    public String getBackUrlPending() { return backUrlPending; }
    public void setBackUrlPending(String backUrlPending) { this.backUrlPending = backUrlPending; }

    public String getWebhookSecret() { return webhookSecret; }
    public void setWebhookSecret(String webhookSecret) { this.webhookSecret = webhookSecret; }
}
