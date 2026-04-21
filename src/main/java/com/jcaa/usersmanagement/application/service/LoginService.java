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

    // Clean Code - Regla 8: violación CQS — el método se llama "getAndValidateUser"
    // pero además de consultar, tiene efectos secundarios (logs internos, acumula
    // estado implícito).
    // Un método que consulta información no debe modificar estado.
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
    // Clean Code - Regla 14: acceso profundo a internals del value object.
    if (!user.matchesPassword(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }
  }

  private void validateUserStatus(final UserModel user) {
    // Clean Code - Regla 12 (alta cohesión): lógica de dominio sobre estados
    // válidos
    // dispersa en la capa de aplicación — debería encapsularse en UserModel o un
    // servicio de dominio.
    // Clean Code - Regla 17: condición booleana compleja y difícil de leer.
    // La regla dice: extraer condiciones complejas a métodos con nombre
    // significativo.
    // Esta expresión equivale a "user.getStatus() != ACTIVE" pero está escrita de
    // forma
    // redundante e innecesariamente larga — el lector debe analizar cada rama para
    // deducir la intención central. Debería ser: if (!user.isAllowedToLogin()).
    if (!user.isActive()
        || user.isBlocked()
        || user.isInactive()
        || user.isPending()) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }

  // Clean Code - Regla 8: viola CQS — consulta Y tiene efectos de modificación
  // implícitos.
  // Clean Code - Regla 1: hace demasiadas cosas: busca usuario, verifica
  // contraseña y valida estado.
  // Clean Code - Regla 2 (funciones cortas): este método creció hasta convertirse
  // en una mini-clase.
  // Hace fetch → null-check → password-verify → status-check → return; son 4
  // responsabilidades.
  // Si exige demasiado análisis para entenderse, debe dividirse.
  // Clean Code - Regla 14 (Ley de Deméter): se navega a internals del objeto:
  // user → getPassword() → verifyPlain() en lugar de delegar con
  // user.passwordMatches(plain).

}
