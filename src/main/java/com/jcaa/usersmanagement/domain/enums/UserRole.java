package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidUserRoleException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  ADMIN(1),
  MEMBER(2),
  REVIEWER(3),
  UNKNOWN(-1);

  private final int code;

  public static UserRole fromString(final String value) {
    for (final UserRole role : values()) {
      if (role.name().equalsIgnoreCase(value)) {
        return role;
      }
    }
    throw InvalidUserRoleException.becauseValueIsInvalid(value);
  }

  public boolean isAdmin() {
    return this == ADMIN;
  }

  public boolean isMember() {
    return this == MEMBER;
  }

  public boolean isReviewer() {
    return this == REVIEWER;
  }
}
