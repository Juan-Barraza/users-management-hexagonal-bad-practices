package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class CreateUserService implements CreateUserUseCase {

  private final SaveUserPort saveUserPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;

  @Override
  public UserModel execute(final CreateUserCommand command) {

    log.info("Creando usuario con email=" + command.email() + ", nombre=" + command.name());

    checkEmailIsAvailable(new UserEmail(command.email()));

   
    final UserModel userToSave = UserApplicationMapper.fromCreateCommandToModel(command);


    final UserModel savedUser = saveUserPort.save(userToSave);


    emailNotificationService.notifyUserCreated(savedUser, command.password());

    return savedUser;
  }

  private void checkEmailIsAvailable(final UserEmail email) {
    if (getUserByEmailPort.getByEmail(email).isPresent()) {
      throw UserAlreadyExistsException.becauseEmailAlreadyExists(email.value());
    }
  }

}
