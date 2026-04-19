package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserIdException extends DomainException {

  private static final String ERROR_USER_ID_EMPTY = "The user id must not be empty.";

  private InvalidUserIdException(final String message) {
    super(message);
  }

  public static InvalidUserIdException becauseValueIsEmpty() {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new InvalidUserIdException(ERROR_USER_ID_EMPTY);
  }
}
