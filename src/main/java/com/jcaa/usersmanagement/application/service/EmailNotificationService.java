package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Log
@RequiredArgsConstructor
public final class EmailNotificationService {

  private static final String SUBJECT_CREATED = "Tu cuenta ha sido creada — Gestión de Usuarios";
  private static final String SUBJECT_UPDATED = "Tu cuenta ha sido actualizada — Gestión de Usuarios";

  private static final String TOKEN_NAME = "name";
  private static final String TOKEN_EMAIL = "email";
  private static final String TOKEN_PASSWORD = "password";
  private static final String TOKEN_ROLE = "role";
  private static final String TOKEN_STATUS = "status";

  private final EmailSenderPort emailSenderPort;

  public void notifyUserCreated(final UserModel user, final String plainPassword) {

    final Map<String, String> model = createBaseModelMap(user);
    model.put(TOKEN_PASSWORD, plainPassword);

    sendNotification(user, SUBJECT_CREATED, "user-created.html", model);
  }

  public void notifyUserUpdated(final UserModel user) {

    final Map<String, String> model = createBaseModelMap(user);
    model.put(TOKEN_STATUS, user.getStatus().name());

    sendNotification(user, SUBJECT_UPDATED, "user-updated.html", model);
  }

  private void sendNotification(UserModel user,
      String subject, String template, Map<String, String> values) {
    String body = processTemplate(template, values);
    EmailDestinationModel destination = buildDestination(user, subject, body);
    sendEmail(destination);
  }

  private static EmailDestinationModel buildDestination(
      final UserModel user, final String subject, final String body) {
    return new EmailDestinationModel(
        user.getEmail().value(), user.getName().value(), subject, body);
  }

  private static String processTemplate(
      final String templateName,
      final Map<String, String> model) {
    return renderTemplate(loadTemplate(templateName), model);
  }

  private static String loadTemplate(final String templateName) {
    final String path = "/templates/" + templateName;
    try (InputStream inputStream = openResourceStream(path)) {
      if (Objects.isNull(inputStream)) {
        throw EmailSenderException.becauseSendFailed(
            new IllegalStateException("Template not found: " + path));
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioException) {
      throw EmailSenderException.becauseSendFailed(ioException);
    }
  }

  static InputStream openResourceStream(final String path) {
    return EmailNotificationService.class.getResourceAsStream(path);
  }

  private static String renderTemplate(String template, final Map<String, String> values) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : values.entrySet()) {
      final String token = "{{" + tokenEntry.getKey() + "}}";
      result = result.replace(token, tokenEntry.getValue());
    }
    return result;
  }

  private void sendEmail(final EmailDestinationModel destination) {
    try {
      emailSenderPort.send(destination);
    } catch (final EmailSenderException senderException) {
      throw senderException;
    }
  }

  private Map<String, String> createBaseModelMap(UserModel user) {
    return new HashMap<>(Map.of(
        TOKEN_NAME, user.getName().value(),
        TOKEN_EMAIL, user.getEmail().value(),
        TOKEN_ROLE, user.getRole().name()));
  }
}
