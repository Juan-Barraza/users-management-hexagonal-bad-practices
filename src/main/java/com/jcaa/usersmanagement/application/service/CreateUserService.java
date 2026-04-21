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
    // Clean Code - Regla 1: cada función debe hacer una sola cosa.
    // Clean Code - Regla 2: las funciones deben ser cortas.
    // Clean Code - Regla 3: un solo nivel de abstracción por función.
    // Este método mezcla: validación de constraints, log de PII, verificación de
    // negocio,
    // construcción del dominio (nivel técnico bajo), persistencia, notificación y
    // retorno.
    // Tiene demasiadas responsabilidades y mezcla niveles de abstracción (reglas de
    // negocio
    // junto con detalles de formateo de strings y construcción manual de objetos de
    // dominio).

    // Clean Code - Regla 9: se usa comentario para tapar un bloque poco expresivo.
    // La regla dice: antes de comentar, intenta mejorar nombres y extraer
    // funciones.

    log.info("Creando usuario con email=" + command.email() + ", nombre=" + command.name());

    // Clean Code - Regla 10: comentario redundante — el código siguiente ya dice lo
    // mismo.
    // verificar si el email ya existe en la base de datos
    checkEmailIsAvailable(new UserEmail(command.email()));

    // Clean Code - Regla 3: aquí se mezcla lógica de negocio de alto nivel (crear
    // usuario)
    // con detalles de construcción de bajo nivel (new UserId, new UserName, etc.).
    // Estos detalles deberían estar encapsulados en el mapper o en una fábrica
    final UserModel userToSave = UserApplicationMapper.fromCreateCommandToModel(command);

    // Clean Code - Regla 10: comentario que explica lo obvio — no aporta valor.
    // guardar el usuario en la base de datos
    final UserModel savedUser = saveUserPort.save(userToSave);

    // Clean Code - Regla 10: otro comentario redundante.
    // enviar notificacion de bienvenida al usuario creado
    emailNotificationService.notifyUserCreated(savedUser, command.password());

    // retornar el usuario guardado
    return savedUser;
  }

  private void checkEmailIsAvailable(final UserEmail email) {
    if (getUserByEmailPort.getByEmail(email).isPresent()) {
      throw UserAlreadyExistsException.becauseEmailAlreadyExists(email.value());
    }
  }

}
