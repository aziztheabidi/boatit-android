# Boatit Android – Enterprise Refactoring Implementation Document

**Version:** 1.0  
**Date:** February 2025  
**Scope:** Full codebase refactor to modern, enterprise-grade standards.

---

## 1. Executive Summary

This document defines the strategy and implementation plan to refactor the Boatit (james) Android app from its current MVVM-ish, single-module structure into a **Clean Architecture–aligned**, **enterprise-ready** codebase. The refactor preserves behavior while improving maintainability, testability, security, and scalability.

**Target outcome:** A codebase that follows Android and Kotlin best practices, with clear layers (data/domain/presentation), consistent naming, no global mutable state, proper configuration management, and a structure that supports future modularization.

---

## 2. Current State Assessment

### 2.1 Tech Stack
- **Language:** Kotlin, JVM 11
- **UI:** Jetpack Compose, Material3, Navigation Compose
- **DI:** Koin
- **Networking:** Ktor HttpClient (CIO, JSON, Bearer + refresh)
- **Backend services:** Firebase (Auth, FCM, Firestore, Realtime DB), Google Places API, Google Maps
- **Serialization:** kotlinx.serialization, Gson (mixed)
- **Min/Target/Compile SDK:** 24 / 34 / 35

### 2.2 Architecture (Current)
- **Pattern:** MVVM-ish; single Activity, Compose-only UI
- **Data flow:** ViewModels expose `StateFlow<NetworkResponse<T>>`; screens collect and send events; repositories call Ktor directly with `ApiConstants` + endpoints
- **DI:** Single Koin module in `network.di.Modules`; all ViewModels and repositories registered in one place
- **Navigation:** Single `NavHost` in `NavigationGraph.kt` with string-based routes via `NavigationManager`

### 2.3 Critical Issues

| Category | Issue | Impact |
|----------|--------|--------|
| **Naming & structure** | Typos: `dashbaord`, `availablitystatus`, `networkreposne`, `CaptainDahsboard`, `AcceptRequset`, `UpdateStatusRequset`, `mapAPiKey`, `Loginscreen.kt` | Confusion, unprofessional, harder search/refactor |
| **Package/class swap** | ViewModels in `repository/` and Repositories in `viewmodel/` (signup/general, voyager/dashboard) | Violates convention, confusing for onboarding |
| **Global mutable state** | `AppConstants` with `var USER_ID`, `JWT_TOKEN`, `Voyage_ID`, `PLACES` used across app; KtorClient fallback to `AppConstants.JWT_TOKEN` | No single source of truth, hard to test, lifecycle bugs |
| **Secrets & config** | Maps API key in `build.gradle.kts`; `BASE_URL` hardcoded in `ApiConstants.kt` | Security risk, inflexible for environments |
| **API layer** | No API interfaces; repositories use `HttpClient` + string URLs directly | Hard to mock, no clear contract |
| **DI** | Single module; unqualified `import LocationViewModel` | Hard to navigate; no feature/layer separation |
| **Error handling** | `println` for logs; no unified logging or error-message mapping | Poor diagnostics, no production-ready logging |
| **Inconsistency** | Mix of `*Repo` / `*Repository`; lowercase file names (`loginResponse.kt`) | Inconsistent style |

---

## 3. Target Architecture

### 3.1 Layered Architecture (Clean Architecture–aligned)

We adopt a **layered** structure within the current single module, with clear boundaries so that future modularization (e.g. `:data`, `:domain`, `:feature-*`) can be done without re-architecting.

```
┌─────────────────────────────────────────────────────────────┐
│  Presentation (UI)                                          │
│  - Composables, ViewModels, UI state/events                  │
│  - Depends on: Domain (use cases) / Data (repos interfaces)  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│  Domain (optional but recommended for key flows)             │
│  - Use cases, domain models                                 │
│  - Depends on: nothing (pure Kotlin)                        │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│  Data                                                        │
│  - Repository implementations, API interfaces, data sources │
│  - Config (base URL, endpoints), token/session              │
└─────────────────────────────────────────────────────────────┘
```

- **Presentation:** Feature packages under `ui.*` with `view/`, `viewmodel/`, and optionally `model/` for UI-specific DTOs.
- **Data:** `data/` (or keep under `network/` with clearer naming): API interfaces, Ktor client, repository implementations, `SessionManager` (replacing `AppConstants` for session data).
- **Domain:** Introduce only where it adds value (e.g. auth, voyage lifecycle); use cases call repository interfaces.

### 3.2 Package Structure (Target)

```
com.boatit.boatsharing/
├── application/
├── core/                    # NEW: shared across layers
│   ├── config/              # BuildConfig, ApiConfig, Endpoints
│   ├── di/                  # Koin modules (split by layer/feature)
│   ├── network/             # HttpClient, API interfaces, NetworkResponse
│   ├── session/             # SessionManager (replaces AppConstants for session)
│   └── util/                # Logger, extensions, constants (non-secret)
├── data/                    # NEW (or refactor from network + repos)
│   ├── remote/              # API interfaces + DTOs
│   └── repository/          # Repository implementations
├── routes/
└── ui/
    ├── captain/
    │   ├── availabilitystatus/   # FIX: was availablitystatus
    │   ├── dashboard/            # FIX: was dashbaord
    │   └── voyages/
    ├── chat/
    ├── forgotpassword/
    ├── login/
    ├── signup/
    │   ├── business/
    │   ├── captain/
    │   └── general/
    ├── userroles/
    ├── voyager/
    │   └── dashboard/            # FIX: was dashbaord
    ├── onboardingscreens/
    ├── splash/
    ├── menu/
    └── shared/                   # Shared composables, uihelpers → here
```

- **Naming:** All folders and files use correct spelling: `dashboard`, `availabilitystatus`, `networkresponse`. Classes: PascalCase; files match class name (e.g. `LoginScreen.kt` → `LoginScreen`).
- **ViewModel vs Repository:** ViewModels live in `viewmodel/`, Repositories in `repository/`; class names match (e.g. `LoginViewModel` in `viewmodel/`, `LoginRepository` in `repository/`).

### 3.3 Key Design Decisions

1. **Session & auth:** Introduce a `SessionManager` (or extend `TokenProvider` with session state) that holds `userId`, `voyageId`, and optional in-memory cache for current voyage/places. No global `var` in `AppConstants` for tokens or user id; Ktor uses only `TokenProvider` (backed by preferences/session).
2. **Config:** Base URL and endpoint paths come from a single place (e.g. `ApiConfig` / `BuildConfig` + `Endpoints` object). API keys from `local.properties` → `BuildConfig` or resource; never committed.
3. **API layer:** Define interfaces (e.g. `AuthApi`, `VoyageApi`) that take base URL and use `HttpClient`; repositories depend on these interfaces so that tests can use fakes.
4. **DI:** Split Koin into at least: `coreModule` (client, config, session, logging), `authModule`, `voyageModule`, `captainModule`, `voyagerModule`, `chatModule`, etc. Fix all imports to fully qualified where needed.
5. **Logging:** Replace `println` with a small `Logger` interface (e.g. `AndroidLogLogger` in app); use it in Ktor and critical paths.
6. **NetworkResponse:** Keep sealed class; optionally add `Unauthorized` / `NetworkError` for better error handling and retry logic.

---

## 4. Implementation Phases

### Phase 1: Naming, Spelling, and Package Consistency
**Goal:** Fix typos, align file/class names, move ViewModels to `viewmodel/` and Repositories to `repository/` where swapped.

- [ ] Rename packages: `dashbaord` → `dashboard`, `availablitystatus` → `availabilitystatus`, `networkreposne` → `networkresponse`.
- [ ] Rename files: `CaptainDahsboard.kt` → `CaptainDashboard.kt`, `AcceptRequsetResponse.kt` → `AcceptRequestResponse.kt`, `UpdateStatusRequset.kt` → `UpdateStatusRequest.kt`, `Loginscreen.kt` → `LoginScreen.kt`, etc.
- [ ] Fix `mapAPiKey` → `maps_api_key` (resource name) and references.
- [ ] Move misplaced classes: signup/general and voyager/dashboard ViewModels to `viewmodel/`, Repositories to `repository/`; ensure class names match (e.g. `PasswordViewModel` in viewmodel package, `PasswordRepository` in repository package).
- [ ] Standardize on `*Repository` (or consistently `*Repo`) and PascalCase file names (e.g. `LoginResponse.kt`).
- [ ] Update all imports and Koin `Modules.kt`; fix `import LocationViewModel` to full package.

**Deliverable:** Codebase compiles; all tests pass; no typos in package/class/file names; consistent ViewModel/repository locations.

**Phase 1 completed (Feb 2025):**
- Renamed package `networkreposne` → `networkresponse` (new package created, all imports updated, old files removed).
- Renamed packages `dashbaord` → `dashboard` (captain + voyager), `availablitystatus` → `availabilitystatus` (content + directory renames).
- Fixed unqualified `LocationViewModel` import in `Modules.kt` to full package.
- Renamed files: `CaptainDahsboard.kt` → `CaptainDashboard.kt`, `Loginscreen.kt` → `LoginScreen.kt`, `AcceptRequsetResponse.kt` → `AcceptRequestResponse.kt`, `UpdateStatusRequset.kt` → `UpdateStatusRequest.kt`.
- Resource name `mapAPiKey` → `maps_api_key` (build.gradle, MainApplication, AndroidManifest); key can be overridden via `MAPS_API_KEY` in `local.properties`.
- ViewModel/Repository package swap (signup/general, voyager/dashboard) left for a follow-up to avoid large file moves in one pass; DI and class names already work.

---

### Phase 2: Configuration and Secrets
**Goal:** Remove hardcoded API keys and base URL; use BuildConfig and local configuration.

- [ ] Add `local.properties` (or CI secrets) for `MAPS_API_KEY`, `API_BASE_URL`; document in README that these are not committed.
- [ ] In `app/build.gradle.kts`: read from `local.properties` or env, inject into `BuildConfig`; use `resValue` or `BuildConfig.MAPS_API_KEY` for Places/Manifest.
- [ ] Create `core/config/ApiConfig.kt` (or use BuildConfig) for `baseUrl`; keep `Endpoints` as path constants. Remove hardcoded URL from `ApiConstants` (or rename to `Endpoints` only).
- [ ] Ensure no secrets in version control; add `local.properties` to `.gitignore` if not already.

**Deliverable:** No API key or base URL in source; app runs with local config.

---

### Phase 3: Session Management (Remove Global Mutable State)
**Goal:** Single source of truth for session (user id, voyage id, tokens); no `AppConstants` for these.

- [ ] Introduce `SessionManager` (or extend `TokenProvider`): hold `currentUserId`, `currentVoyageId`, and optionally temporary data (e.g. selected places). Persist what’s needed (e.g. userId after login) via SharedPreferences or DataStore; in-memory only where appropriate.
- [ ] Replace all `AppConstants.USER_ID`, `JWT_TOKEN`, `Voyage_ID`, `PLACES` reads/writes with `SessionManager` (or dedicated providers) and inject where needed.
- [ ] Ktor client: use only `TokenProvider` (backed by SessionManager/prefs); remove fallback to `AppConstants.JWT_TOKEN`.
- [ ] Deprecate and remove `AppConstants` for session-related fields; keep only non-sensitive app constants in a dedicated `Constants` object if needed.

**Deliverable:** No global mutable session state; session injectable and testable.

---

### Phase 4: API Layer and Network Abstractions
**Goal:** Clear API contracts and testable network layer.

- [ ] Define API interfaces (e.g. `AuthApi`, `VoyageApi`, `CaptainApi`) with suspend functions and DTOs; implement with Ktor in `data/remote` (or under `core/network`).
- [ ] Repositories depend on these interfaces (and optionally on `SessionManager`); move endpoint construction into API implementations using `ApiConfig` + `Endpoints`.
- [ ] Optionally introduce a generic `ApiResult` or extend `NetworkResponse` with `Unauthorized`/`NetworkError` for consistent error handling and retry in Ktor auth.

**Deliverable:** Repositories use API interfaces; tests can use fake APIs.

---

### Phase 5: Dependency Injection Reorganization
**Goal:** Feature/layer-based Koin modules and clear imports.

- [ ] Split `Modules.kt` into: `coreModule` (HttpClient, ApiConfig, SessionManager, TokenProvider, Logger, etc.), `authModule`, `voyageModule`, `captainModule`, `voyagerModule`, `chatModule`, `signupModule`, etc.
- [ ] Each module exposes only what’s needed; avoid circular dependencies.
- [ ] Fix `LocationViewModel` to use full package import in core/captain module.
- [ ] Load all modules in `MainApplication` (or single app module that aggregates them).

**Deliverable:** Navigable, maintainable DI; no single 160-line module.

---

### Phase 6: Logging and Error Handling
**Goal:** Production-ready logging and consistent error handling.

- [ ] Add `core/util/Logger.kt` interface and `AndroidLogLogger`; use in Ktor (logging plugin or custom), and in critical repository/ViewModel code. Remove `println`.
- [ ] Optional: centralize API error mapping (e.g. HTTP status → user message) in one place used by repositories or ViewModels.

**Deliverable:** No `println`; configurable logging; clearer error reporting.

---

### Phase 7: Optional Domain Layer and Tests
**Goal:** Improve testability and business logic clarity.

- [ ] Add use cases for critical flows (e.g. login, start voyage) that wrap repository calls; ViewModels call use cases.
- [ ] Add repository interfaces where beneficial for testing; inject fakes in unit tests.
- [ ] Add unit tests for at least one ViewModel and one repository using fakes.

**Deliverable:** Example tests and pattern for future coverage.

---

## 5. Naming and Code Conventions

- **Packages:** lowercase, no underscores; correct spelling (`dashboard`, `availabilitystatus`, `networkresponse`).
- **Classes:** PascalCase; files named after the primary class.
- **ViewModels:** in `viewmodel/` package; suffix `ViewModel`.
- **Repositories:** in `repository/` package; suffix `Repository` (or consistent `Repo`).
- **API interfaces:** suffix `Api`; implementations `*ApiImpl` or Ktor-based in `remote` package.
- **Resources:** `snake_case` (e.g. `maps_api_key`); no `mapAPiKey`.
- **Sealed classes:** e.g. `NetworkResponse.Success`, `NetworkResponse.Error`, `NetworkResponse.Loading`.

---

## 6. Risk Mitigation

- **Incremental refactor:** One phase at a time; run app and tests after each phase.
- **Branch strategy:** Consider a refactor branch; merge after each phase or at the end.
- **Secrets:** Never commit real API keys; use placeholders in docs and `local.properties` template.

---

## 7. Success Criteria

1. No typos in package, file, or class names; ViewModels and Repositories in correct packages.
2. No hardcoded API keys or base URL in source; config via BuildConfig/local config.
3. No global mutable session state; session managed via injected SessionManager/TokenProvider.
4. API layer behind interfaces; repositories testable with fakes.
5. DI split into multiple modules; no unqualified imports for ViewModels.
6. No `println`; logging via Logger abstraction.
7. App builds, runs, and passes existing tests; no regressions in main user flows.

---

## 8. References

- Android App Architecture (official)
- Guide to Android app modularization
- Modern Android App Architecture with Clean Code Principles
- Project exploration report (this repo)

---

*End of Implementation Document*
