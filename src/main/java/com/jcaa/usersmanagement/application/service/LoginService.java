package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;

  @Override
  public UserModel execute(final LoginCommand command) {
    final UserEmail email = new UserEmail(command.email());
    final UserModel user = findUserOrThrow(email);
    verifyPassword(user, command.password());
    validateUserStatus(user);

    return user;
  }

  private UserModel findUserOrThrow(final UserEmail email) {
    final UserModel user = getUserByEmailPort.getByEmail(email)
        .orElseThrow(InvalidCredentialsException::becauseCredentialsAreInvalid);
    return user;
  }

  private void verifyPassword(final UserModel user, final String plainPassword) {
    if (!user.matchesPassword(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }
  }

  private void validateUserStatus(final UserModel user) {
    if (!user.isAllowedToLogin()) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }

}
