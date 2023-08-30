package com.julienbock.keycloak.actions;

import org.jboss.logging.Logger;
import org.keycloak.authentication.InitiatedActionSupport;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.credential.PasswordCredentialProvider;
import org.keycloak.credential.PasswordCredentialProviderFactory;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.services.validation.Validation;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.ModelException;
import org.keycloak.common.util.Time;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.events.Errors;
import org.keycloak.events.Details;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.services.messages.Messages;

import java.util.function.Consumer;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

public class CreatePasswordRequiredAction implements RequiredActionProvider {

    private static final Logger logger = Logger.getLogger(CreatePasswordRequiredAction.class);

    public static final String ID = "CREATE_PASSWORD";

    private final KeycloakSession session;
    public CreatePasswordRequiredAction(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public InitiatedActionSupport initiatedActionSupport() {
        return InitiatedActionSupport.SUPPORTED;
    }

    @Override
    public void evaluateTriggers(RequiredActionContext context) {

        // check whether we need to show the update custom info form.

        if (!ID.equals(context.getAuthenticationSession().getClientNotes().get("kc_action"))) {
            // only show update form if we explicitly asked for the required action execution
            return;
        }

        context.getUser().addRequiredAction(ID);
    }


    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        Response response = context.form()
            .setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
            .createForm("create-password-form.ftl");

        context.challenge(response);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        EventBuilder event = context.getEvent();
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        RealmModel realm = context.getRealm();
        UserModel user = context.getUser();
        KeycloakSession session = context.getSession();

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        event.event(EventType.UPDATE_PROFILE);

        String passwordNew = formData.getFirst("password-new");
        String passwordConfirm = formData.getFirst("password-confirm");

        EventBuilder errorEvent = event.clone().event(EventType.UPDATE_PROFILE_ERROR)
                .client(authSession.getClient())
                .user(authSession.getAuthenticatedUser());

        if (Validation.isBlank(passwordNew)) {
            Response challenge = context.form()
                    .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                    .addError(new FormMessage(Validation.FIELD_PASSWORD, Messages.MISSING_PASSWORD))
                    .createForm("create-password-form.ftl");
            context.challenge(challenge);
            errorEvent.error(Errors.PASSWORD_MISSING);
            return;
         } else if (!passwordNew.equals(passwordConfirm)) {
            Response challenge = context.form()
                    .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                    .addError(new FormMessage(Validation.FIELD_PASSWORD_CONFIRM, Messages.NOTMATCH_PASSWORD))
                    .createForm("create-password-form.ftl");
            context.challenge(challenge);
            errorEvent.error(Errors.PASSWORD_CONFIRM_ERROR);
            return;
        }

        try {
            user.credentialManager().updateCredential(UserCredentialModel.password(passwordNew, false));
            context.success();
        } catch (ModelException me) {
            errorEvent.detail(Details.REASON, me.getMessage()).error(Errors.PASSWORD_REJECTED);
            Response challenge = context.form()
                .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                .setError(me.getMessage(), me.getParameters())
                .createForm("create-password-form.ftl");
            context.challenge(challenge);
            return;
        } catch (Exception ape) {
            errorEvent.detail(Details.REASON, ape.getMessage()).error(Errors.PASSWORD_REJECTED);
            Response challenge = context.form()
                .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                .setError(ape.getMessage())
                .createForm("create-password-form.ftl");
            context.challenge(challenge);
            return;
       }
    }

    @Override
    public void close() {
    }

}