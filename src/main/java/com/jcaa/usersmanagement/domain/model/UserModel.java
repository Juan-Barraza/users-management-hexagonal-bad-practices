package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
// VIOLACIÓN Regla 9 (Hexagonal): el dominio importa una clase de infraestructura.
// Las dependencias siempre deben ir hacia el centro — nunca desde el dominio hacia afuera.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

// Clean Code - Regla 15 (inmutabilidad como preferencia de diseño):
// Se cambió @Value por @Data + @AllArgsConstructor, lo que expone setters públicos
// para todos los campos. Un modelo de dominio debe ser inmutable: los setters permiten
// que cualquier clase modifique el estado del objeto sin pasar por invariantes ni
// reglas de negocio.
// Con @Value todos los campos serían final y no habría setters.
// Con @Data + @AllArgsConstructor cualquiera puede hacer userModel.setStatus(BLOCKED)
// desde fuera del dominio, rompiendo el encapsulamiento.
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
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
  // VIOLACIÓN Regla 9 (Hexagonal): método de conversión a entidad de
  // infraestructura dentro del dominio.
  // El dominio NO debe saber nada sobre cómo se persisten sus datos.
}
