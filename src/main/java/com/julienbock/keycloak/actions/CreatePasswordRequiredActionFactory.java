package com.julienbock.keycloak.actions;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@AutoService(RequiredActionFactory.class)
public class CreatePasswordRequiredActionFactory implements RequiredActionFactory {

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return new CreatePasswordRequiredAction(session);
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public String getId() {
        return CreatePasswordRequiredAction.ID;
    }

    @Override
    public String getDisplayText() {
        return "Create password when user was created";
    }

    @Override
    public boolean isOneTimeAction() {
        return true;
    }
}
