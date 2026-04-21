package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserEmailException extends DomainException {

  private static final String ERROR_EMAIL_EMPTY = "The user email must not be empty.";
  private static final String ERROR_EMAIL_FORMAT = "The user email format is invalid: '%s'.";

  private InvalidUserEmailException(final String message) {
    super(message);
  }

  public static InvalidUserEmailException becauseValueIsEmpty() {
    return new InvalidUserEmailException(ERROR_EMAIL_EMPTY);
  }

  public static InvalidUserEmailException becauseFormatIsInvalid(final String email) {
    return new InvalidUserEmailException(String.format(ERROR_EMAIL_FORMAT, email));
  }
}
