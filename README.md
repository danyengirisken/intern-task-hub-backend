# Intern Task Hub — Backend

Eğitim amaçlı **Intern Task Hub** backend'i. Yapı, **carbon-backend** projesinin
konvansiyonları referans alınarak kurulmuştur (profil bazlı config, ayrı
`database-*.properties`, `liquibase/` changeSet düzeni, `S_` tablo prefiksi,
`SEQ_` sequence'ler, `Dao`/`services` paketleri). Stack modern tutulmuştur
(Spring Boot 3.5 / Java 17, sade JWT).

## Teknolojiler
Spring Boot 3.5 · Java 17 · Maven · PostgreSQL · Spring Data JPA · Liquibase ·
Spring Security · JWT (jjwt) · Lombok · Bean Validation

## Konfigürasyon (carbon konvansiyonu)

Aktif profil Maven `activatedProperties` ile gelir (varsayılan **dev**):

| Dosya | İçerik |
|-------|--------|
| `application.properties` | Ortak ayarlar; `spring.profiles.active=@activatedProperties@`, JPA, Liquibase, JWT |
| `application-dev.properties` | Ortama özel (port 8082, log seviyeleri) |
| `database-dev.properties` | **Veritabanı bağlantısı** → `jdbc:postgresql://localhost:5432/interndb` |

`InterntaskhubApplication`, `@PropertySources` ile aktif profile göre hem
`application-<profil>` hem `database-<profil>` dosyalarını yükler.

> Veritabanı adını/parolasını değiştirmek için `database-dev.properties`'i düzenleyin.
> Bu bağlantıyı **DBeaver** ile de açıp tabloları inceleyebilirsiniz.

## Veritabanı

`interndb` veritabanı PostgreSQL'de mevcut olmalı (siz oluşturuyorsunuz):
```sql
CREATE DATABASE interndb;
```
Tablolar ve başlangıç verisi **Liquibase** ile otomatik oluşturulur
(Hibernate `ddl-auto=validate` — tablo oluşturmaz, yalnızca doğrular).

## Çalıştırma

> ⚠️ Sistem `JAVA_HOME` JDK 8'i gösterdiğinden Maven varsayılan olarak JDK 8
> kullanır ve `record`/Java 17 derlenemez. `run.ps1` JAVA_HOME'u JDK 17 yapar.
> IntelliJ kullanıyorsanız Project SDK = **17** seçin.

```powershell
.\run.ps1
# veya:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd spring-boot:run            # dev profili (varsayılan)
.\mvnw.cmd -P prod spring-boot:run    # prod profili
```
Backend: **http://localhost:8082**

## Başlangıç Kullanıcıları

| Kullanıcı | Şifre | Rol |
|-----------|-------|-----|
| admin | 123456 | ADMIN |
| intern | 123456 | INTERN |

## Login API
`POST /api/auth/login` → `{ token, tokenType, user{id,fullName,username,role}, menus[] }`

## Liquibase Yapısı (carbon stili)
```
resources/liquibase/
 ├── interntaskhub-changelog.xml          (master)
 └── changeSets/
      ├── 1_POSTGRESQL_CREATE_S_ROLE.xml       (sequence + tablo)
      ├── 2_POSTGRESQL_CREATE_S_MENU.xml
      ├── 3_POSTGRESQL_CREATE_S_USER.xml
      ├── 4_POSTGRESQL_CREATE_S_ROLE_MENU.xml
      └── 5_POSTGRESQL_INITDATA.sql            (başlangıç verisi)
```
Tablolar: `S_ROLE`, `S_MENU`, `S_USER`, `S_ROLE_MENU` · Sequence'ler: `SEQ_S_*`

## Paket Yapısı (carbon stili)
```
com.danyengirisken.interntaskhub
 ├── config        WebSecurityConfiguration
 ├── controller    AuthController            (@CrossOrigin)
 ├── entity        Auditable, Role, Menu, User      (S_ tabloları, sequence ID)
 │    └── dto      LoginRequest, LoginResponse, UserDto, MenuDto
 ├── exception     GlobalExceptionHandler, ApiErrorResponse
 ├── repository    UserDao, RoleDao, MenuDao        (Dao suffix)
 ├── security      JwtService, JwtAuthenticationFilter, CustomUserDetailsService
 └── services      AuthService + AuthServiceImpl     (interface + impl)
```

## Carbon'dan alınmayanlar (uyumsuz/gereksiz)
- OAuth2 Authorization Server (`spring-security-oauth2`) → Boot 3'te kaldırıldı; yerine sade JWT.
- `com.abt.fw` framework bağımlılığı.
- Hibernate Envers `_AUD` audit tabloları (Auditable kolonları korundu, Envers yok).
- Oracle tip adları (NUMBER/varchar2) → PostgreSQL tipleri (BIGINT/VARCHAR/TIMESTAMP).
