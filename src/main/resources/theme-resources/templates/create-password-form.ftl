<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('password','password-confirm'); section>
    <#if section = "header">
        ${msg("configurationPasswordTitle")}
    <#elseif section = "form">
    <div id="kc-form">
        <#if message?has_content && (message.type != 'warning') >
            <div class="alert-${message.type} ${properties.kcAlertClass!} pf-m-warning">
                <div class="pf-c-alert__icon">
                    <span class="${properties.kcFeedbackWarningIcon!}"></span>
                </div>
                <span class="${properties.kcAlertTitleClass!}">${kcSanitize(msg("configurationPasswordMessage"))?no_esc}</span>
            </div>
        </#if>
      <div id="kc-form-wrapper">
        <form id="kc-passwd-update-form" class="${properties.kcLoginForm!}" action="${url.loginAction}" method="post">
            <input type="text" id="username" name="username" value="${username}" autocomplete="username"
                   readonly="readonly" style="display:none;"/>
            <input type="password" id="password" name="password" autocomplete="current-password" style="display:none;"/>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.LoginFieldContainer!}">
                    <input type="password" id="password-new" name="password-new" placeholder="${msg("passwordNew")}"
                        class="${properties.kcInputClass!} ${properties.LoginField!}"
                       autofocus autocomplete="new-password"
                       aria-invalid="<#if messagesPerField.existsError('password','password-confirm')>true</#if>"
                    />
                    <label for="password-new" class="${properties.kcLabelClass!} ${properties.LoginLabel}">${msg("passwordNew")}</label>
                </div>

                <#if messagesPerField.existsError('password')>
                    <span id="input-error-password" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                        ${kcSanitize(messagesPerField.get('password'))?no_esc}
                    </span>
                </#if>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.LoginFieldContainer!}">
                    <input type="password" id="password-confirm" name="password-confirm" placeholder="${msg("passwordConfirm")}"
                       class="${properties.kcInputClass!} ${properties.LoginField!}"
                       autocomplete="new-password"
                       aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>"
                    />
                    <label for="password-confirm" class="${properties.kcLabelClass!} ${properties.LoginLabel}">${msg("passwordConfirm")}</label>
                </div>
                <#if messagesPerField.existsError('password-confirm')>
                    <span id="input-error-password-confirm" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                        ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                    </span>
                </#if>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                        <#if isAppInitiatedAction??>
                            <div class="checkbox">
                                <label><input type="checkbox" id="logout-sessions" name="logout-sessions" value="on" checked> ${msg("logoutOtherSessions")}</label>
                            </div>
                        </#if>
                    </div>
                </div>

                <div id="kc-form-buttons" >
                    <#if isAppInitiatedAction??>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}" />
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" type="submit" name="cancel-aia" value="true" />${msg("doCancel")}</button>
                    <#else>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}" />
                    </#if>
                </div>
            </div>
        </form>
        </div>
    </div>
    </#if>
</@layout.registrationLayout>