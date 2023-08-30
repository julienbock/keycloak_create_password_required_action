package com.julienbock.keycloak.email;

import org.keycloak.Config;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.email.EmailTemplateProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class CustomEmailTemplateProviderFactory implements EmailTemplateProviderFactory {

    public static final String PROVIDER_ID = "customEmailProvider";

    @Override
    public EmailTemplateProvider create(KeycloakSession session) {
        return new CustomEmailTemplateProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

}