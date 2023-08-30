# KEYCLOAK EXTENSION CREATE PASSWORD REQUIRED ACTION

KEYCLOAK 21.1.1

- Julien Bock https://github.com/julienbock
- Alexandre Aznar https://github.com/aznar-alexandre

Keycloak extension to create new required action who can be use with API REST execute-action-email (cf keycloak admin rest API)

This .jar contain a required action and a custom email template provider. The template email provider is for override system who sendExecuteAction and pass a special key to use translation on email subject.

## Environment Variables

If you want use the custom template email provider you need to add this environment variable in your keycloak

`KC_SPI_EMAIL_TEMPLATE_PROVIDER: "customEmailProvider"`

or add this options when you run with kc.sh start (start-dev)

`--spi-email-template-provider=customEmailProvider`

cf: https://www.keycloak.org/server/configuration-provider#_configuring_a_default_provider

## Features

- Create Password Required Action
- Custom Email Template Provider

## Build

To build project just launch
`mvn clean package`
or
Download .jar in this repository

## Install in keycloak

Put the .jar in your folder providers/ on your keycloak instance

## Use translation key in email template

```
1 <#outputformat "plainText">
2    <#assign requiredActionName><#if requiredActions??><#list requiredActions as reqActionItem>${reqActionItem}</#list></#if></#assign>
3 </#outputformat>
4
5 <#import "template.ftl" as layout>
6 <@layout.mailLayout ; section>
7
8  ...
9    <#if section="title">
10      <span>${kcSanitize(msg("requiredAction.${requiredActionName}.executeActionTitle"))}</span>
11    </#if>
12  ...
13 </@layout.mailLayout>
```

In line 2 we use the name of requiredAction and use this variable line 10. In translation file we do that `messages_fr.properties` :

```
requiredAction.CREATE_PASSWORD=Configurer un mot de passe
requiredAction.CREATE_PASSWORD.executeActionTitle=Finalisons la création de votre compte
requiredAction.CREATE_PASSWORD.executeActionsSubject=Bienvenue, cr\u00e9ez votre votre mot de passe
```

You can see the name of required action **CREATE_PASSWORD**
