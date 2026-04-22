# Phase 0 Architecture Conventions

These rules are active for refactor work to avoid regressions and style drift.

## Naming and Package Rules

- Fix typo packages in controlled migrations only (example: `dashbaord` to `dashboard`).
- New feature code should follow:
  - `feature/<name>/data`
  - `feature/<name>/domain`
  - `feature/<name>/presentation`
- Do not put `*ViewModel` classes under repository folders.
- Keep API model classes separated from UI state models.

## Layer Boundaries

- Presentation layer:
  - Compose screens
  - UI state/effects/events
  - ViewModels only
- Domain layer:
  - Use-cases and business rules
  - No Android framework dependencies
- Data layer:
  - Repositories
  - Remote/local data sources
  - DTO mappers

## DI Binding Ownership (Phase 1)

- `coreModule`:
  - Cross-cutting infra bindings only
  - Ktor client setup, token/status providers, shared pref manager
  - Firebase instances, location provider, global notification/location viewmodels

- `authModule`:
  - Login, registration-temp, verify-email, password, forgot-password
  - Voyager profile setup/get during onboarding
  - No chat, captain, business, or voyage feature bindings

- `voyagerModule`:
  - Voyager dashboard flows and voyage operations
  - Find boat, booking, sponsor payments, travel now, voyage lists

- `captainModule`:
  - Captain profile/docs/boat onboarding
  - Captain voyage actions (accept/start/complete/cancel/feedback)
  - Captain availability/status and captain voyage list

- `businessModule`:
  - Business onboarding/profile/info/logo/about
  - Business dashboard/get/save bindings

- `chatUserrolesModule`:
  - Chat repositories/viewmodels
  - Follow/follower interactions
  - Role + FCM token bindings

- `Modules.kt` (aggregator only):
  - Must only `includes(...)` feature modules
  - Must not contain direct `single {}` / `viewModel {}` bindings

## Safety Rules

- Avoid route and DTO contract changes in refactor-only PRs.
- Keep changes incremental and feature-scoped.
- Add/maintain tests for touched critical flows.
- Run phase-0 pre-merge safety script before merge.
- All new repositories must use shared mapper helpers (`toResult`, `networkFailure`) for status/error mapping.

## Required Commands

From workspace root:

```powershell
Set-Location "c:/Users/syeda/Documents/boatsharingapp"
./scripts/pre-merge-safety.ps1 -BackendBaseUrl "http://127.0.0.1:5265"
```
