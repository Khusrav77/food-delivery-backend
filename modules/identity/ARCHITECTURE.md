# Identity Module Architecture

## 1. Purpose

`identity` отвечает за identity/authentication domain:

* идентификацию пользователя по номеру телефона;
* создание `UserAccount`;
* генерацию и хранение OTP;
* проверку OTP;
* управление состоянием аккаунта.

Модуль не должен зависеть от конкретных инфраструктурных технологий.

---

## 2. Architecture

Используем **Ports & Adapters (Hexagonal Architecture)**.

Основное правило зависимостей:

```text
Inbound Adapter
      ↓
Input Port (UseCase)
      ↓
Application Service
      ↓
Output Port
      ↓
Outbound Adapter
```

Domain не зависит от Application и Infrastructure.

```text
                ┌─────────────────┐
                │     Domain      │
                │                 │
                │ UserId          │
                │ PhoneNumber     │
                │ UserAccount     │
                │ AccountStatus   │
                │ OtpCode         │
                └────────▲────────┘
                         │
                ┌────────┴────────┐
                │   Application   │
                │                 │
                │ Commands        │
                │ Results         │
                │ UseCases        │
                │ Services        │
                │ Input/Output    │
                │ Ports           │
                └────────▲────────┘
                         │
                ┌────────┴────────┐
                │    Adapters     │
                │                 │
                │ REST            │
                │ PostgreSQL      │
                │ Redis           │
                │ OTP provider    │
                └─────────────────┘
```

---

## 3. Domain

### UserId

`UserId` — value object, представляющий идентификатор пользователя.

ID генерируется приложением:

```java
UserId.generate()
```

используя `UUID.randomUUID()`.

Это позволяет создавать идентификатор до сохранения в PostgreSQL и не связывать domain с механизмом генерации ID базы данных.

---

### PhoneNumber

`PhoneNumber` — value object для номера телефона.

Отвечает за базовую валидность значения:

* `null` запрещён;
* blank значение запрещено.

---

### UserAccount

`UserAccount` — aggregate root identity domain.

Содержит:

```text
UserId
PhoneNumber
AccountStatus
```

Поддерживает переходы состояний:

```text
ACTIVE → BLOCKED
ACTIVE → DISABLED
BLOCKED → ACTIVE
```

`DISABLED` аккаунт нельзя снова активировать и нельзя заблокировать.

Создание:

```java
UserAccount.create(UserId.generate(), phone)
```

---

### OtpCode

`OtpCode` — domain object, отвечающий за жизненный цикл OTP.

Хранит:

```text
value
expiresAt
attempts
used
```

Правила:

* OTP состоит из 6 цифр;
* TTL задаётся application service;
* после expiration OTP нельзя проверить;
* после успешной проверки OTP становится `used`;
* максимум 5 попыток;
* использованный OTP нельзя проверить повторно.

Domain API:

```java
OtpCode.create(value, expiresAt);

otpCode.verify(candidate, now);

otpCode.isExpired(now);
```

Domain не знает, где OTP хранится и как он генерируется.

---

# 4. Application Layer

Application layer координирует use cases и взаимодействует с внешним миром только через ports.

Используется общий контракт:

```java
public interface UseCase<I, R> {
    R execute(I input);
}
```

---

## Commands

Commands описывают входные данные use case.

```text
CreateUserAccountCommand
RequestOtpCommand
VerifyOtpCommand
```

Command не содержит application business logic.

---

## Results

Results описывают результат выполнения use case.

Например:

```text
RequestOtpResult
VerifyOtpResult
```

---

## Input Ports

Input ports являются интерфейсами application use cases:

```text
CreateUserAccountUseCase
RequestOtpUseCase
VerifyOtpUseCase
```

Они наследуются от:

```java
UseCase<I, R>
```

---

## Application Services

Реализации use cases находятся в:

```text
application/service
```

Текущие сервисы:

```text
CreateUserAccountService
RequestOtpService
VerifyOtpService
```

`CreateUserAccountService` и `RequestOtpService` уже реализованы.

`VerifyOtpService` находится в процессе разработки.

---

# 5. Output Ports

Application layer не работает напрямую с PostgreSQL, Redis, system clock или OTP generator.

Для этого используются output ports.

Текущие:

```text
UserAccountPort
OtpCodePort
ClockPort
OtpGeneratorPort
```

### UserAccountPort

```java
Optional<UserAccount> findByPhone(PhoneNumber phone);

UserAccount save(UserAccount userAccount);
```

### OtpCodePort

```java
void save(PhoneNumber phone, OtpCode otp);

Optional<OtpCode> findByPhone(PhoneNumber phone);

void deleteByPhone(PhoneNumber phone);
```

### ClockPort

Application service получает текущее время через порт вместо прямого вызова:

```java
Instant.now()
```

Это позволяет детерминированно тестировать время.

### OtpGeneratorPort

Генерирует OTP.

Application layer не знает, каким способом он генерируется.

---

# 6. Current Authentication Flow

## Request OTP

Текущий flow:

```text
RequestOtpCommand
        │
        ▼
RequestOtpService
        │
        ├── ClockPort.now()
        │
        ├── OtpGeneratorPort.generate()
        │
        ├── OtpCode.create(value, expiresAt)
        │
        └── OtpCodePort.save(phone, otp)
        │
        ▼
RequestOtpResult
```

TTL OTP определяется application service:

```text
3 minutes
```

`RequestOtpService` работает с `PhoneNumber`.

Он не должен знать `UserId` и не отвечает за создание `UserAccount`.

---

## Create User Account

```text
CreateUserAccountCommand
        │
        ▼
CreateUserAccountService
        │
        ▼
UserAccountPort.findByPhone()
        │
        ├── exists → return existing account
        │
        └── not exists
                │
                ▼
        UserId.generate()
                │
                ▼
        UserAccount.create(...)
                │
                ▼
        UserAccountPort.save()
```

Таким образом, создание аккаунта является отдельным use case.

---

## Verify OTP

Планируемый flow:

```text
VerifyOtpCommand
        │
        ▼
VerifyOtpService
        │
        ├── OtpCodePort.findByPhone()
        │
        ├── OtpCode.verify(candidate, now)
        │
        └── получить UserAccount
                │
                ▼
        VerifyOtpResult
```

Детали результата успешной authentication ещё не зафиксированы.

JWT/session management не должен смешиваться с domain `OtpCode`.

---

# 7. Testing Strategy

Domain тестируется без Spring:

```text
OtpCodeTest
UserAccountTest
UserIdTest
```

Application services также тестируются без Spring.

Используются fake implementations output ports:

```text
FakeUserAccountPort
FakeOtpCodePort
FakeClock
FakeOtpGenerator
```

Пример:

```text
RequestOtpService
       │
       ├── FakeClock
       ├── FakeOtpGenerator
       └── FakeOtpCodePort
```

Это позволяет тестировать application logic независимо от PostgreSQL, Redis и Spring.

---

# 8. Architectural Rules

### Rule 1 — Domain independence

Domain не должен зависеть от:

```text
Spring
JPA
PostgreSQL
Redis
Kafka
HTTP
JWT
```

---

### Rule 2 — Application через ports

Application services не должны напрямую использовать:

```java
Instant.now();
```

или конкретные repositories/adapters.

Используются соответствующие ports.

---

### Rule 3 — Commands на входе

Input use cases принимают Commands:

```java
execute(Command command)
```

а не набор отдельных параметров.

---

### Rule 4 — Domain behavior в domain

Правила OTP находятся в `OtpCode`.

Правила состояния аккаунта находятся в `UserAccount`.

Application service только координирует use case.

---

### Rule 5 — Infrastructure outside

Конкретные реализации:

```text
PostgreSQL repository
Redis OTP storage
SecureRandom OTP generator
Spring Clock adapter
REST controllers
```

будут добавляться как adapters, не изменяя domain.

---

# 9. Current Status

```text
Domain
├── UserId                 ✅
├── PhoneNumber            ✅
├── AccountStatus          ✅
├── UserAccount            ✅
└── OtpCode                ✅

Application
├── UseCase<I, R>          ✅
├── Commands               ✅
├── Results                ✅
├── CreateUserAccount      ✅
├── RequestOtp             ✅
└── VerifyOtp              🚧

Input Ports
├── CreateUserAccount      ✅
├── RequestOtp             ✅
└── VerifyOtp              ✅

Output Ports
├── UserAccountPort        ✅
├── OtpCodePort            ✅
├── ClockPort              ✅
└── OtpGeneratorPort       ✅

Tests
├── Domain tests           ✅
├── CreateUserAccount      ✅
├── RequestOtp              🚧
└── VerifyOtp               ⏳

Infrastructure Adapters
└── Not implemented yet
```

---

# 10. Next Steps

Порядок дальнейшей реализации:

```text
1. Complete RequestOtpService tests
        ↓
2. Implement VerifyOtpService
        ↓
3. VerifyOtpService tests
        ↓
4. Define VerifyOtpResult
        ↓
5. Authentication/session boundary
        ↓
6. PostgreSQL adapters
        ↓
7. OTP storage adapter
        ↓
8. REST inbound adapters
        ↓
9. Spring configuration / composition root
```

Основная цель — сохранить независимость domain и application layers от инфраструктуры и возможность в дальнейшем заменить PostgreSQL/Redis/HTTP implementation без изменения domain logic.
