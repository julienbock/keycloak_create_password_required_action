package com.julienbock.keycloak.email;

import java.util.Map;
import java.util.HashMap;

import org.keycloak.email.EmailException;
import org.keycloak.email.freemarker.FreeMarkerEmailTemplateProvider;
import org.keycloak.models.KeycloakSession;

public class CustomEmailTemplateProvider extends FreeMarkerEmailTemplateProvider {

    public CustomEmailTemplateProvider(KeycloakSession session) {
        super(session);
    }

    @Override
    public void sendExecuteActions(String link, long expirationInMinutes) throws EmailException {
        Map<String, Object> attributes = new HashMap<>(this.attributes);
        addLinkInfoIntoAttributes(link, expirationInMinutes, attributes);

        String keySubject = "defaultExecuteActionsSubject";
        if (attributes.containsKey("requiredActions")) {
            Object requiredAction = attributes.get("requiredActions");
            keySubject = "requiredAction." + requiredAction.toString().replace("[", "").replace("]", "") + ".executeActionsSubject";
        }
        send(keySubject, "executeActions.ftl", attributes);
    }
}
