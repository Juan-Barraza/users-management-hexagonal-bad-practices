package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserRoleException extends DomainException {

  private static final String ERROR_ROLE_INVALID = "The user role '%s' is not valid.";

  private InvalidUserRoleException(final String message) {
    super(message);
  }

  public static InvalidUserRoleException becauseValueIsInvalid(final String role) {
    return new InvalidUserRoleException(String.format(ERROR_ROLE_INVALID, role));
  }
}
