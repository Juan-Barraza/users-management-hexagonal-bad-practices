package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserStatusException extends DomainException {

  private static final String ERROR_STATUS_INVALID = "The user status '%s' is not valid.";

  private InvalidUserStatusException(final String message) {
    super(message);
  }

  public static InvalidUserStatusException becauseValueIsInvalid(final String status) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new InvalidUserStatusException(String.format(ERROR_STATUS_INVALID, status));
  }
}
