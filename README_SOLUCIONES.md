# Solución de Violaciones de Reglas (Capas de Dominio y Aplicación)

Este documento detalla las soluciones propuestas para las violaciones de las **Reglas 1** (Buenas Prácticas Java y Arquitectura Hexagonal) y **Reglas 2** (Clean Code) identificadas en el código.

---

## Solución Regla 1 (Buenas Prácticas Java)

### Rama: `--fix/regla1/violacion1` (Manejo de Nulos y Estilo)
- **Problema:** Uso de `obj == null` o `obj != null` no usar imports con `*`, y metodos sin estado  `statics.
- **Solución:** Reemplazar por `Objects.isNull(obj)`, `Objects.nonNull(obj)` o `Objects.requireNonNull(obj)` agregar `static` a los metodos sin estado e importar sin `*`.
- **Regla:** Regla 4 (Estilo y Naming).

### Rama: `--fix/regla1/violacion2` (Logging y PII)
- **Problema:** Logs en la capa de dominio y logueo de PII (email del usuario) y try catch redundantes.
- **Solución:** Eliminar el `Logger` de `UserEmail`. En `DeleteUserService`, usar la anotación `@Log` de Lombok en lugar de un logger manual y no loguear datos sensibles. Quitar el try catch redundante.
- **Regla:** Regla 6 (Exeptions, Logging y Telemetría).

### Rama: `--fix/regla1/violacion3` (Arquitectura Hexagonal - Independencia)
- **Problema:** El dominio importa `UserEntity` (infraestructura) y contiene un método `toEntity()`.
- **Solución:** Eliminar el import de `UserEntity` y el método `toEntity()`. La conversión debe realizarse en un Mapper dentro de la capa de `infrastructure` o `application`.
- **Regla:** Regla 9 (Arquitectura Hexagonal).

### Rama: `--fix/regla1/violacion4` (Constantes y Magic Numbers)
- **Problema:** Valores como `8`, `12`, `3` y mensajes de error están hardcodeados.
- **Solución:** Definir constantes `private static final` con nombres descriptivos (p. ej., `MINIMUM_PASSWORD_LENGTH`, `BCRYPT_COST`, `ERROR_MSG_USER_NOT_FOUND`).
- **Regla:** Regla 10 (Calidad del Código).

### Rama: `--fix/regla1/violacion5` (Lombok y Validaciones)
- **Problema:** `@Valid` en implementaciones, uso de `@Builder` en `record`, y mensajes personalizados en constraints.
- **Solución:** Mover `@Valid` al puerto (interfaz). Eliminar `@Builder` de los records (innecesario). Quitar el atributo `message` de las anotaciones de validación para usar los mensajes por defecto.
- **Regla:** Regla 3 (Lombok y Validaciones).

---

## Solución Regla 2 (Clean Code)

### Rama: `--fix/regla2/violacion1` (Diseño de Funciones y CQS)
- **Problema:** Funciones complejas, niveles de abstracción mezclados y efectos secundarios.
- **Solución:** Dividir funciones, aplicar CQS y mejorar claridad secuencial.
- **Regla:** Reglas 1, 2, 3, 4, 5, 6, 7, 8, 25, 26 y 27 (Clean Code).

### Rama: `--fix/regla2/violacion2` (Deméter e Inmutabilidad)
- **Problema:** Acoplamiento profundo, objetos mutables y baja cohesión.
- **Solución:** Aplicar Ley de Deméter, preferir `@Value` y eliminar utilitarios innecesarios.
- **Regla:** Regla 12, 13, 14, 15 y 20 (Clean Code).

### Rama: `--fix/regla2/violacion3` (Expresividad y Factory Methods)
- **Problema:** Lógica condicional opaca y falta de semántica en la construcción.
- **Solución:** Usar Factory Methods, simplificar condiconales y eliminar comentarios redundantes.
- **Regla:** Regla 9, 10, 16, 17 y 18 (Clean Code).

### Rama: `--fix/regla2/violacion4` (Consistencia y Errores)
- **Problema:** Duplicación de lógica, inconsistencia semántica y códigos ambiguos.
- **Solución:** Centralizar reglas (DRY), estandarizar nombres y evitar el uso de `null`.
- **Regla:** Regla 11, 19, 21, 22, 23 y 24 (Clean Code).
