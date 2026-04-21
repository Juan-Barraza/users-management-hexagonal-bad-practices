package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.Objects;

public class UserApplicationMapper {
  private static final int UNKNOWN_ROLE_CODE = -1;

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    final String userId = command.id();
    final String userName = command.name();

    final String email = command.email();
    final String userPass = command.password();
    final String userRole = command.role();

    return UserModel.create(
        new UserId(userId),
        new UserName(userName),
        new UserEmail(email),
        UserPassword.fromPlainText(userPass),
        UserRole.fromString(userRole));
  }

  public static UserModel fromUpdateCommandToModel(
      final UpdateUserCommand command, final UserPassword currentPassword) {

    UserPassword passwordToUse;
    if (Objects.isNull(command.password()) || command.password().isBlank()) {
      passwordToUse = currentPassword;
    } else {
      passwordToUse = UserPassword.fromPlainText(command.password());
    }

    final String email = command.email();

    return UserModel.builder()
        .id(new UserId(command.id()))
        .name(new UserName(command.name()))
        .email(new UserEmail(email))
        .password(passwordToUse)
        .role(UserRole.fromString(command.role()))
        .status(UserStatus.fromString(command.status()))
        .build();
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  public static int roleToCode(final String roleName) {
    try {
      return UserRole.fromString(roleName).getCode();
    } catch (Exception e) {
      return UNKNOWN_ROLE_CODE;
    }
  }
}
