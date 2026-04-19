package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserPasswordException extends DomainException {

  private static final String ERROR_PASSWORD_EMPTY = "The user password must not be empty.";
  private static final String ERROR_PASSWORD_TOO_SHORT = "The user password must have at least %d characters.";

  private InvalidUserPasswordException(final String message) {
    super(message);
  }

  public static InvalidUserPasswordException becauseValueIsEmpty() {
    return new InvalidUserPasswordException(ERROR_PASSWORD_EMPTY);
  }

  public static InvalidUserPasswordException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserPasswordException(
        String.format(ERROR_PASSWORD_TOO_SHORT, minimumLength));
  }
}
