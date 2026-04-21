package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  private static final String ERROR_USER_NAME_EMPTY = "The user name must not be empty.";
  private static final String ERROR_USER_NAME_TOO_SHORT = "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    return new InvalidUserNameException(ERROR_USER_NAME_EMPTY);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserNameException(
        String.format(ERROR_USER_NAME_TOO_SHORT, minimumLength));
  }
}
