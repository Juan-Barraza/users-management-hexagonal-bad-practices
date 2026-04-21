package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public final class GetUserByIdService implements GetUserByIdUseCase {

  private final GetUserByIdPort getUserByIdPort;

  @Override
  public UserModel execute(final GetUserByIdQuery query) {

    final UserId userId = UserApplicationMapper.fromGetUserByIdQueryToUserId(query);
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }


}
