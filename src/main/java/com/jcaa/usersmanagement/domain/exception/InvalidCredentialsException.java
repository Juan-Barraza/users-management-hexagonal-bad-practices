package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  private static final String ERROR_CREDENTIALS_INVALID = "Correo o contraseña incorrectos.";
  private static final String ERROR_USER_NOT_ACTIVE = "Tu cuenta no está activa. Contacta al administrador.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    return new InvalidCredentialsException(ERROR_CREDENTIALS_INVALID);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    return new InvalidCredentialsException(ERROR_USER_NOT_ACTIVE);
  }
}
