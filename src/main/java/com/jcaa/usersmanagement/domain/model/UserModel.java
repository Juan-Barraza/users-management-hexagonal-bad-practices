package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;

import lombok.AccessLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserModel {

  UserId id;
  UserName name;
  UserEmail email;
  UserPassword password;
  UserRole role;
  UserStatus status;

  public static UserModel create(
      final UserId id,
      final UserName name,
      final UserEmail email,
      final UserPassword password,
      final UserRole role) {
    return new UserModel(id, name, email, password, role, UserStatus.PENDING);
  }

  public UserModel activate() {
    return this.toBuilder().status(UserStatus.ACTIVE).build();
  }

  public UserModel deactivate() {
    return this.toBuilder().status(UserStatus.INACTIVE).build();
  }

  public boolean matchesPassword(final String plainPassword) {
    return this.password.verifyPlain(plainPassword);
  }

  public boolean isActive() {
    return this.status.isActive();
  }

  public boolean isInactive() {
    return this.status.isInactive();
  }

  public boolean isBlocked() {
    return this.status.isBlocked();
  }

  public boolean isPending() {
    return this.status.isPending();
  }

  public boolean isAdmin() {
    return this.role.isAdmin();
  }

  public boolean isMember() {
    return this.role.isMember();
  }

  public boolean isAllowedToLogin() {
    return isActive() && isAdmin() || isActive() && isMember();
  }

  public boolean isSameUser(final UserId userId) {
    return this.id.equals(userId);
  }

}
