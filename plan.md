# Frontend Refactor Plan (james_boat)

## 1) Current Baseline (Codebase Analysis)

This plan is based on current project state in `james_boat/app/src/main/java`.

- Kotlin source files: ~281
- ViewModel files: ~60
- Repository files: ~56
- Unit tests: 1 example test only
- Instrumentation tests: 1 example test only
- DI composition is centralized in a single large module file
- Navigation is centralized and route handling is string-based
- Multiple very large UI files exist (many 400+ lines, one ~900+ lines)
- Package naming inconsistencies exist (for example `dashbaord` typo across many files)

## 2) Main Problems To Solve

### A. Architecture fragmentation
- Feature logic is spread across view/viewmodel/repository with inconsistent boundaries.
- Some folders use inverted naming (for example classes ending with `ViewModel` inside repository folders).
- Global mutable app state (`AppConstants`) creates hidden coupling and side effects.

### B. Scalability and maintainability risk
- Several composable screens are very large and state-heavy.
- Too many mutable UI state variables are managed directly inside screens.
- Repeated patterns exist across Voyager/Captain/Business flows but are not standardized.

### C. Navigation and contracts risk
- Route constants and argument parsing rely on strings and force unwrap (`!!`) patterns.
- Hard to evolve safely without regressions.

### D. Networking and reliability risk
- Networking setup has mixed concerns in one place (client config, auth refresh behavior, defaults).
- API models and repository result contracts are not normalized around one domain result strategy.

### E. Quality gap
- No meaningful test coverage baseline for viewmodels/use-cases/navigation/serialization.
- No enforceable quality gates for refactor safety.

## 3) Refactor Goals

- Create a feature-first architecture with clear boundaries.
- Standardize state flow: UI state + one-off effects + events.
- Remove hidden global mutable state from business logic.
- Stabilize navigation with typed routes/arguments.
- Improve reliability with strong tests and CI gates.
- Execute incrementally without product freeze.

## 4) Execution Strategy

Use incremental vertical slices, not big-bang rewrite.

- Keep app releasable after each phase.
- Migrate one feature cluster at a time.
- Introduce adapters/shims where needed so old and new code can coexist temporarily.
- Add tests before moving critical flows.

## 5) Phase-Wise Plan

## Phase 0: Backend-First Safety Rails and Baseline
Duration: 3-5 days

Scope:
- Start from backend to ensure frontend refactor does not break API behavior.
- Lock API contract and runtime health before any broader frontend rewiring.
- Freeze risky renames until dependency mapping is complete.

Tasks:
- Backend baseline gate (first):
  - `dotnet restore peter/Boat_Sharing.sln`
  - `dotnet build peter/Boat_Sharing.sln -c Debug`
  - Runtime health probe on active API instance (`/swagger/index.html` and key auth endpoint checks)
- Capture and freeze API contract for critical flows used by app:
  - login/refresh/session
  - voyage create/book/confirm/cancel
  - business dashboard read/update
- Add backend non-breaking smoke checklist (run before and after each refactor batch).
- Add frontend quality checks after backend gate passes:
  - ktlint/detekt baseline
  - `assembleDebug`, lint, unit tests
- Document naming/folder conventions and dependency map.

Deliverables:
- Backend build+runtime baseline report (green).
- API contract snapshot for critical endpoints.
- Non-breaking smoke checklist for backend and frontend.
- Quality gates running locally and in CI.

Exit criteria:
- Backend is build-clean and runtime-healthy before any frontend migration PR.
- Critical API contracts are unchanged or explicitly versioned.
- Team can detect regressions automatically before merge.

## Phase 1: Foundation Layer Hardening
Duration: 1-2 weeks

Scope:
- Stabilize core platform pieces used by all features.

Tasks:
- Split DI into modular files: `core`, `auth`, `voyager`, `captain`, `business`, `chat`.
- Normalize repository result handling to one sealed result contract.
- Isolate Ktor auth/refresh/retry concerns behind a clean network module.
- Introduce environment config abstraction (dev/stage/prod) instead of scattered constants.

Deliverables:
- Modular DI registration.
- Standard network response abstraction and error mapping.

Exit criteria:
- No new feature depends directly on old mixed network contract.

## Phase 2: Package and Naming Migration
Duration: 1 week

Scope:
- Resolve structural inconsistencies with minimal behavior change.

Tasks:
- Migrate typo packages (`dashbaord` -> `dashboard`) in controlled batches.
- Fix inverted folder naming (repository vs viewmodel mismatch).
- Introduce consistent package template for every feature:
  - `feature/<name>/data`
  - `feature/<name>/domain`
  - `feature/<name>/presentation`

Deliverables:
- Consistent package map and migration checklist.

Exit criteria:
- No new code added to legacy typo/inconsistent packages.

## Phase 3: State Management Standardization
Duration: 2 weeks

Scope:
- Standardize screen architecture and remove ad-hoc mutable state spread.

Tasks:
- Define a base UI contract:
  - `UiState`
  - `UiEvent`
  - `UiEffect`
- Migrate top-risk large screens first:
  - voyager booking flow screens
  - captain current voyages flow
  - signup account flows
- Move validation/business rules from composables to viewmodels/use-cases.

Phase 3 kickoff order (locked by current file-size baseline):
1. `ui/business/view/BusinessDashboard.kt` (~914 lines)
2. `ui/captain/voyages/view/CaptainCurrentVoyages.kt` (~544 lines)
3. `ui/voyager/dashboard/view/FindBoat.kt` (~538 lines)

Execution order:
- First migrate #1 as pilot for shared `UiState/UiEvent/UiEffect` pattern.
- Then migrate #2 to validate pattern against captain voyage lifecycle complexity.
- Then migrate #3 to validate voyager booking-search flow with map/search interactions.

Deliverables:
- At least top 10 largest screens converted to standardized state pattern.

Exit criteria:
- Converted screens have deterministic state rendering with no business rules in composable bodies.

## Phase 4: Domain Extraction and Global State Removal
Duration: 2 weeks

Scope:
- Remove hidden shared mutable state and centralize domain logic.

Tasks:
- Replace `AppConstants` mutable runtime data with scoped state stores/use-cases.
- Move booking/session/shared journey data into explicit state holder(s).
- Create use-case layer for critical flows:
  - login/session refresh
  - create voyage
  - booking/confirmation/cancellation
  - business profile update

Deliverables:
- Critical flows independent from global singleton mutable fields.

Exit criteria:
- No feature-critical flow reads/writes business runtime data via global mutable constants.

## Phase 5: Navigation Refactor
Duration: 1-1.5 weeks

Scope:
- Safer navigation with typed contracts.

Tasks:
- Replace string route argument handling with typed route helpers.
- Remove force unwrap patterns in navigation argument extraction.
- Introduce one navigation facade per feature module.

Deliverables:
- Typed route builders/parsers for critical navigation paths.

Exit criteria:
- Crash-prone route argument parsing paths removed from critical user journeys.

## Phase 6: Testing Expansion
Duration: 2 weeks (parallel with Phases 3-5)

Scope:
- Build confidence for refactor velocity.

Tasks:
- Add unit tests for use-cases/viewmodels (target 60%+ on migrated modules).
- Add repository tests with mocked network responses.
- Add Compose UI tests for core flows:
  - login
  - voyage create-book-confirm
  - business dashboard update
- Add regression test matrix for API serialization contracts.

Deliverables:
- Real test suites replacing example tests.

Exit criteria:
- Refactor PRs blocked unless tests pass for touched modules.

## Phase 7: Performance, Security, and Release Hardening
Duration: 1 week

Scope:
- Production readiness after structural migration.

Tasks:
- Review secure storage/token invalidation edge cases.
- Add startup/perf tracing on heavy screens.
- Remove dead code and legacy adapters.
- Run full smoke suite on physical devices.

Deliverables:
- Release candidate branch with migration cleanup complete.

Exit criteria:
- No legacy bridge usage in critical flows; release checklist signed off.

## 6) Priority Order (What To Start First)

1. Phase 0 backend gate immediately (non-breaking baseline first).
2. Phase 1 (foundation) before large feature rewrites.
3. Phase 2 (package cleanup) in small safe batches.
4. Phase 3 + Phase 6 in parallel (state migration with tests).
5. Phase 4 (global state removal) once state contracts are stable.
6. Phase 5 navigation hardening.
7. Phase 7 release hardening.

## 7) Recommended First Sprint Backlog (Week 1)

- Lock backend baseline (`restore + build + runtime health probe`) and save report.
- Create backend API contract checklist for mobile critical paths.
- Add lint/detekt + CI checks.
- Split DI file into initial `core` + `auth` + `voyager` modules.
- Create state contract template (`UiState/UiEvent/UiEffect`).

## 8) Risks and Mitigation

- Risk: Broad package rename creates merge conflicts.
  - Mitigation: rename by feature cluster and freeze unrelated PR merges during rename windows.

- Risk: Behavior regressions in booking and payment flows.
  - Mitigation: add contract tests and smoke scripts before migration.

- Risk: Team drift in architecture style.
  - Mitigation: enforce PR template with architecture checklist.

## 9) Definition of Done For Entire Refactor

- Consistent feature-first package structure across modules.
- Standardized state architecture for all major screens.
- Global mutable runtime state removed from core journeys.
- Typed navigation contracts in critical flows.
- Meaningful automated test coverage in business-critical modules.
- CI quality gates preventing regression merges.
- Legacy adapters and dead code cleaned up.

## 10) Phase 0 Automation Commands

Run from workspace root:

```powershell
Set-Location "c:/Users/syeda/Documents/boatsharingapp"
./scripts/backend-contract-smoke.ps1 -BaseUrl "http://127.0.0.1:5265"
./scripts/pre-merge-safety.ps1 -BackendBaseUrl "http://127.0.0.1:5265"
```

PR checklist template:
- `scripts/PHASE0_PR_CHECKLIST.md`

Implemented Phase 0 artifacts:
- `scripts/backend-contract-smoke.ps1`
- `scripts/pre-merge-safety.ps1`
- `scripts/PHASE0_PR_CHECKLIST.md`
- `james_boat/docs/phase0/ARCHITECTURE_CONVENTIONS.md`
- `james_boat/docs/phase0/DEPENDENCY_MAP.md`

Phase 0 implementation status:
- Completed: backend baseline gate automation and contract smoke checks.
- Completed: combined pre-merge script with backend-first flow and optional static analysis.
- Completed: frontend `phase0Safety` Gradle task (`assembleDebug`, `testDebugUnitTest`, `lintDebug`).
- Completed: CI verification stage updated to run phase-0 gates.
- Completed: lint blocker fix in `uihelpers/MapPickerScreen.kt` for unremembered state.

Phase 1 start status:
- Started: DI modularization with new modules `coreModule`, `authModule`, and `voyagerModule`.
- Started: legacy `Modules` now aggregates modular DI via `includes(...)` for incremental migration.
- Validation: `phase0Safety` passed after Phase 1 initial split.
- Completed: extracted `captainModule`, `businessModule`, and `chatUserrolesModule`.
- Completed: moved userroles bindings from `authModule` into `chatUserrolesModule`.
- Completed: slimmed `Modules.kt` to aggregator-only includes.
- Validation: `phase0Safety` passed after full Phase 1 extraction step.
- Completed: DI binding ownership section added in architecture conventions.
- Started: network contract normalization via shared mapper utility (`NetworkContractMapper`).
- Started: migrated `LoginRepository`, `ForgotPassRepository`, and `VoyagerVoyagesRepository` to shared mapper pattern (no endpoint behavior changes).
- Validation: `phase0Safety` passed after normalization slice.
- Continued: migrated next voyager/chat repository batch to mapper pattern:
  - `VoyagerFeedbackRepository`
  - `FutureVoyagesRepo`
  - `SponcerVoyagesRepo`
  - `GetActiveVoyageRepository`
  - `FollowRepository`
  - `VoyagersRepository`
- Completed: added rule that all new repositories must use `toResult` and `networkFailure` helpers.
- Continued: migrated additional repository batch to mapper pattern:
  - `FindBoatRepo`
  - `FetchBusinessRepo`
  - `ConfirmBookedVoyageRepository`
  - `CancelBookedVoyageRepository`
  - `BookVoyageRepo`
  - `PaymentRepository`
  - `SponsorPaymentSheetConfigRepository`
  - `SponsorPaymentConfirmationRepository`
- Continued: migrated remaining voyager/chat normalization batch to mapper pattern:
  - `TravelNowRepo`
  - `FollowedVoyagerRepository`
  - `FollowBusinessRepository`
  - `FetchNearByVoyagesRepo`
  - `FetchCategoryRepo`
  - `FollowRepository` (chat)
  - `VoyagersRepository` (chat)
- Validation: `pre-merge-safety.ps1` passed after latest normalization batch.
- Completed: network normalization pass-1 complete for primary voyager + chat HTTP repositories.

Phase 2 kickoff status:
- Started: typo-package migration discovery (`dashbaord` -> `dashboard`) baseline captured.
- Verified baseline: 135 files and 622 references still contain `dashbaord` in `app/src/main/java`.
- Completed batch-1: captain module package/folder migration (`ui.captain.dashbaord` -> `ui.captain.dashboard`).
- Validation: `phase0Safety` passed after captain package migration batch.
- Completed batch-2: voyager module package/folder migration (`ui.voyager.dashbaord` -> `ui.voyager.dashboard`).
- Validation: `phase0Safety` passed after voyager package migration batch.
- Remaining after batch-2: 0 files and 0 references contain `dashbaord` in `app/src/main/java`.
- Close-out mini batch: stale import/order cleanup completed for captain dashboard package declarations and dependent imports.
- Validation: final `pre-merge-safety.ps1` passed after close-out cleanup.
- Completed: Phase 2 formally complete (package and naming typo migration target achieved).
- Started: Phase 3 kickoff baseline captured and top-3 migration order locked (BusinessDashboard -> CaptainCurrentVoyages -> FindBoat).
- Completed: Phase 3 screen #1 starter slice on `BusinessDashboard` with explicit `UiState`/`UiEvent`/`UiEffect` contract wiring (`BusinessDashboardContract`, `onEvent`, `uiEffects`, screen collector).
- Validation: `:app:compileDebugKotlin` passed after BusinessDashboard contract migration slice.
- Completed: Phase 3 screen #2 starter slice on `CaptainCurrentVoyages` with explicit `UiState`/`UiEvent`/`UiEffect` contract wiring (`CaptainCurrentVoyagesContract`, `onEvent`, `uiEffects`, screen event dispatch).
- Validation: `phase0Safety` passed after CaptainCurrentVoyages contract migration slice.
- Started: Phase 3 screen #3 (`FindBoat`) contract foundation wired in voyager dashboard layer (`FindBoatContract`, `IFindBoatViewModel`, `FindBoatViewModel` `uiState/uiEffects/onEvent`) while preserving existing screen behavior.
- Validation: `phase0Safety` passed after starting the FindBoat migration slice.
- Completed: Phase 3 screen #3 starter slice by wiring `FindBoat` composable interactions to `FindBoat` contract events/effects (`Submit`, selection updates, passenger validation dialog effect handling).
- Validation: `phase0Safety` passed after `FindBoat` event/effect wiring.
- Completed: Phase 3 screen #3 follow-up slice making `FindBoat` composable state rendering contract-driven (`uiState` for fields, dropdown visibility, dialog state).
- Validation: `phase0Safety` passed after `FindBoat` `uiState` rendering alignment.
- Started: next high-risk voyager booking flow migration on `CreateVoyageScreen` with contractized event/effect flow (`CreateVoyageUiEvent`, `CreateVoyageUiEffect`, `ICreateVoyageViewModel`) and `onEvent`-driven UI actions.
- Completed: `CreateVoyageScreen` success-path navigation moved to one-shot `uiEffect` (`NavigateToRateCalculation`) from viewmodel.
- Validation: `phase0Safety` passed after `CreateVoyageScreen` contract migration slice.
- Started: next CreateVoyage booking-path slice for `CreateVoyageSponsorScreen` by replacing direct repository response branching with `UiEvent/UiEffect` handling on `BookVoyageViewModel` + `FindBoatViewModel`.
- Completed: `CreateVoyageSponsorScreen` now dispatches submit actions via `BookVoyageUiEvent`/`FindBoatUiEvent` and consumes one-shot effects for success/error navigation and dialog state updates.
- Validation: `phase0Safety` passed after CreateVoyage sponsor-path contract migration slice.
- Started: `CreateVoyageRateCalcScreen` contract migration by extracting local/action state into dedicated `UiState/UiEvent/UiEffect` contract and `CreateVoyageRateCalcViewModel`.
- Completed: `CreateVoyageRateCalcScreen` now renders from `uiState`, dispatches actions via `onEvent`, and navigates to sponsor step via one-shot `NavigateToSponsor` effect.
- Validation: `phase0Safety` passed after CreateVoyage rate-calc contract migration slice.
- Started: deterministic sponsor-screen state pass by moving remaining loading/error/dialog flags from `CreateVoyageSponsorScreen` local state into `BookVoyageUiState` and `FindBoatUiState`.
- Completed: `CreateVoyageSponsorScreen` now consumes `bookUiState`/`findUiState` for submit loading and both error dialogs, with dismiss actions routed through `BookVoyageUiEvent` and `FindBoatUiEvent`.
- Validation: `phase0Safety` passed after sponsor-screen uiState/dialog migration slice.
- Started: sponsor static-display contractization by introducing a single sponsor-specific uiState source for `total fare`, `pickup/dropoff`, `individual fare`, and `sponsor count`.
- Completed: `CreateVoyageSponsorScreen` display rendering now reads from `CreateVoyageSponsorUiState` (`CreateVoyageSponsorViewModel`) instead of direct `AppConstants` field access in composable rendering paths.
- Validation: `phase0Safety` passed after sponsor static-display uiState migration slice.
- Started: sponsor list interaction contractization for `SponsorScreen` add/remove/update actions under the same sponsor-specific contract.
- Completed: sponsor add/remove row interactions now dispatch contract events (`AddSponsor` / `RemoveSponsor` / `ToggleSponsorSelection`) via `CreateVoyageSponsorUiEvent`, and booking payload in `CreateVoyageSponsorScreen` now consumes `sponsorUiState.sponsorEntries`.
- Validation: `phase0Safety` passed after sponsor interaction contract migration slice.
- Started: final sponsor determinism slice to migrate `SponsorScreen` search query and followed-voyager filtering state out of `VoyagersListViewModel` into `CreateVoyageSponsor` contract state/events.
- Completed: `SponsorScreen` now reads `searchQuery`, `isVoyagersLoading`, `voyagersLoadError`, and `filteredFollowedVoyagers` directly from `CreateVoyageSponsorUiState`; search updates now dispatch `UpdateSearchQuery` event and voyager reload uses `LoadFollowedVoyagers` event.
- Completed: `CreateVoyageSponsorViewModel` now owns followed-voyager fetch + filter derivation (`VoyagersRepository`) so sponsor branch state is under one explicit contract path.
- Safety rule active: run `pre-merge-safety.ps1` after each rename batch before moving to next slice.

Remaining refactor scope (latest):
- Started: sponsor amount-edit deterministic slice to wire selected sponsor amount field in `SponsorScreen` to `CreateVoyageSponsorUiEvent.UpdateSponsorAmount`.
- Completed: selected sponsors in `SponsorScreen` now render amount input and dispatch `UpdateSponsorAmount` on edit, making add/edit/remove all contract-event driven from UI.
- Started: remove sponsor-flow `AppConstants.Estimated_Cost` mutation path from `CreateVoyageSponsorViewModel` and derive individual fare deterministically from contract state (`total cost / sponsor count`).
- Completed: sponsor add/remove/update transitions now recalculate sponsor amounts and individual fare via deterministic state helpers; `CreateVoyageSponsorScreen` booking payload now sends `IndvidualAmount` from sponsor contract state instead of shared-global estimated cost.
- Started: sponsor legacy cleanup pass and Phase 3 closeout checklist verification.
- Completed: final sponsor legacy cleanup review passed (`SponsorScreen`/`CreateVoyageSponsorScreen`/`CreateVoyageSponsorViewModel` deterministic contract path validated; no remaining sponsor-branch `AppConstants.Estimated_Cost` mutation points).
- Validation: `phase0Safety` passed after deterministic cost transition + cleanup pass.
- Completed: Phase 3 sponsor branch closeout checklist done.
- Started: Phase 4 kickoff slice (global state removal) on sponsor flow by replacing submit-path live `AppConstants` reads with contract-owned booking snapshot fields.
- Completed: `CreateVoyageSponsorViewModel` now captures booking draft snapshot in `CreateVoyageSponsorUiState` (`voyagerUserId`, voyage ids, date/time, cost, route docks), and `CreateVoyageSponsorScreen` book/find requests now consume those contract fields.
- Ongoing (Phase 4): continue replacing remaining critical-flow `AppConstants` runtime reads/writes with explicit scoped state holders.
- Started: Phase 4 continuation slice introducing shared scoped holder `CreateVoyageDraftStore` for RateCalc->Sponsor booking draft data.
- Completed: `CreateVoyageSponsor` initialize path now reads split/travel-now + route labels from `CreateVoyageDraftStore` (not runtime `AppConstants` lookups), and sponsor contract initialize event is now VM-owned (`Initialize` object).
- Completed: `CreateVoyageRateCalcViewModel` now seeds and updates `CreateVoyageDraftStore` and no longer bootstraps sponsor list via `AppConstants.sponsorList` mutation.
- Next (Phase 4): migrate remaining legacy write-through compatibility points (`AppConstants.sponsorList` / `AppConstants.Voyage_ID`) behind domain-scoped stores/use-cases.
- Started: Phase 4 follow-up slice to remove remaining sponsor-flow compatibility write-through points.
- Completed: removed sponsor write-through in `CreateVoyageSponsorViewModel` (`AppConstants.sponsorList` no longer updated there); sponsor list source for the flow is now scoped `CreateVoyageDraftStore` state.
- Completed: introduced `VoyageSessionStore` and migrated booking success path to store voyage id there instead of writing `AppConstants.Voyage_ID` in `CreateVoyageSponsorScreen`.
- Completed: `VoyageBookedScreenVoyager` now consumes voyage id from `VoyageSessionStore` (with safe fallback read-only compatibility), and clears session store after payment completion/error.
- Completed: removed `AppConstants.Voyage_ID` fallback path by migrating remaining voyage-id producers/consumers in adjacent entry points (`DashboardScreen`, `BusinessListScreen`, `CaptainDashboard`, `VoyageStartedScreen`, `VoyageDetails`) to `VoyageSessionStore` or typed voyage model ids.
- Completed: removed `CreateVoyageDraftStore.ensureInitializedFromGlobals()` bootstrap; draft store now receives typed upstream handoff from `CalculateFairViewModel` (`setDraft`) and downstream screens consume scoped draft state.
- Validation: `:app:compileDebugKotlin` passed after end-to-end migration updates.
- Validation: `phase0Safety` passed after this Phase 4 continuation slice.
- Completed: removed remaining direct `CreateVoyageScreen` AppConstants write (`Travel_Now`) and shifted CreateVoyage input/fare fields (`Event_Date`, `Event_Time`, `Event_Time_End`, `No_of_Hour`, `Per_Hour_Rate`, `Estimated_Cost`, `Total_Cost`) to typed ui-state + draft-store handoff in `CalculateFairViewModel`.
- Completed: migrated `FindBoat` contract/state to carry typed upstream ids (`voyagerUserId`, `categoryId`, `pickupDockId`, `dropOffDockId`) and seed `CreateVoyageDraftStore` directly from contract state in `FindBoatViewModel` (removed global write-throughs for these fields).
- Completed: removed `CreateVoyage` compatibility reads for `USER_ID`, dock/category ids, and stay-on-water from `CalculateFairViewModel`; draft handoff now consumes `CreateVoyageDraftStore` + local `uiState` only.
- Completed: migrated `FindBoat` cached list sources to contract state (`categoryOptions`, `dockOptions`) and removed dropdown dependence on shared cached globals (`AppConstants.Cates`, `AppConstants.PLACES`) in this booking path.
- Completed: replaced business-to-findboat prefill globals (`AppConstants.BusinessDock`, `AppConstants.BusinessDockTYpe`) with scoped `FindBoatPrefillStore` consumed by `BusinessDetail`, `DashboardScreen`, and `FindBoat`.
- Completed: removed remaining `FindBoat` passenger-count write-through to `AppConstants.No_Of_Voyagers`.
- Completed: removed remaining shared list globals outside `FindBoat` path by deleting `AppConstants.PLACES` / `AppConstants.Cates` write-through in `NearByVoyagesViewModel` and switching `FindDestinationLocationScreen` list rendering to scoped `NearByVoyagesViewModel.nearbyPlaces` state.
- Completed: migrated adjacent voyager business flow (`BusinessListScreen` -> `BusinessDetail`) off runtime globals `AppConstants.Business` / `AppConstants.Business_Status` by introducing scoped `BusinessSelectionStore` handoff/state.
- Completed: migrated captain USER_ID runtime dependencies in `CaptainDahsboard`, `CaptainCurrentVoyages`, and `LocationViewModel` to a single scoped session source (`UserSessionStore`) wired through `coreModule`.
- Validation: `:app:compileDebugKotlin` and `phase0Safety` passed after captain USER_ID cleanup slice.
- Completed: removed runtime `AppConstants` dependencies from the next residual dashboard/detail batch by migrating `BusinessDashboardViewModel`, `BusinessDashboard`, `Dashboardscreen`, `VoyageDetails`, and `FutureVoyageItems` to scoped/session-backed values (`UserSessionStore`) and local static hour/image sources.
- Validation: `:app:compileDebugKotlin` and `phase0Safety` passed after business/voyager residual cleanup slice.
- Completed: removed the next captain/voyager USER_ID residual set by migrating `CaptainStatus`, `CaptainVoyageDetails`, `CaptainVoyagesRepo`, `CaptainActiveVoyagesRepo`, `ConfirmVoyage`, `SponsorVoyagesItems`, `TravelNowItem`, and `VoyageBookedVoyager` off `AppConstants` user/session paths to `UserSessionStore` and scoped draft/session store clears.
- Validation: no IDE diagnostics on touched files after this slice (`get_errors` clean across all edited targets).
- Completed: removed the next targeted captain/voyager/business residual set by migrating `UpdateStatus` to `UserSessionStore`, removing stale runtime-global imports in `CaptainVoyages`/`StartedRequestTab`/`VoyageStarted`/`RateYourVoyage`, removing `NearByVoyagesViewModel` business-place global cache write-through, and replacing `BusinessDetail` image base-path global usage with local resource-backed path reads.
- Validation: `:app:compileDebugKotlin` and `phase0Safety` passed after this slice; IDE diagnostics are clean on all touched files.
- Completed: removed the next voyager residual repository/request global paths by migrating `VoyagerVoyagesRepo`, `SponcerVoyagesRepo`, `GetActiveVoyageRepo`, `FutureVoyagesRepo`, and `FollowedVoyagerRepo` from runtime `AppConstants.USER_ID` reads to `UserSessionStore` injection through `voyagerModule`.
- Completed: removed remaining calculate-fare runtime globals by changing `CalculateFairRepo` to consume typed booking parameters (`fromDockId`, `toDockId`, `voyageCategoryId`, `noOfVoyagers`, `durationInHours`) from `CalculateFairViewModel` + `CreateVoyageDraftStore` instead of `AppConstants` lookups.
- Completed: removed adjacent voyager/business/captain residual `AppConstants` references in `VoyageStartedVoyager`, `VoyagerVoyages`, `StartVoyage`, `BusinessListScreen`, `ConfirmBookedVoyageViewModel`, `CancelBookedVoyageViewModel`, `ConfirmBooking`, `GoogleDirectionsApi`, `GetBusinessRepo`, `GetBusinessDocsRepo`, and stale commented references in `AcceptVoyagerRequest`.
- Validation: IDE diagnostics are clean across the workspace (`get_errors` returned no errors) after this completion batch.
- Started: Phase 4 domain hardening pass on booking/confirmation/cancellation seams by introducing explicit domain use-cases (`BookVoyageUseCase`, `ConfirmBookedVoyageUseCase`, `CancelBookedVoyageUseCase`) and migrating `BookVoyageViewModel`, `ConfirmBookedVoyageViewModel`, and `CancelBookedVoyageViewModel` to consume use-cases rather than repositories directly.
- Completed: wired voyager DI (`voyagerModule`) to provide booking/confirm/cancel use-cases with repository-backed gateway lambdas, reducing presentation-to-data coupling in this critical flow.
- Started: Phase 5 typed navigation pass for the same booking flow by adding `VoyagerFlowRoutes` typed route builders/patterns and migrating critical callsites (`CreateVoyageRateCalc`, `CreateVoyageSponsor`, `VoyageBookedVoyager`) plus `NavigationGraph` route patterns off raw string interpolation.
- Started: Phase 6 testing pass for touched modules by adding focused unit tests for new seams (`VoyageBookingUseCasesTest`, `VoyagerFlowRoutesTest`).
- Validation: file-level diagnostics are clean on all touched source and test files after this slice; terminal task execution remains intermittently unstable in this environment due recurring interactive batch prompt interruptions.
- Completed: extended Phase 4 domain hardening to adjacent voyager flows by introducing use-case seams for payment confirmation and active/future voyage actions (`ConfirmVoyagePaymentUseCase`, `ConfirmSponsorPaymentUseCase`, `FetchActiveVoyageUseCase`, `FetchFutureVoyagesUseCase`) and migrating `PaymentViewModel`, `SponsorPaymentConfirmationViewModel`, `GetActiveVoyageViewModel`, and `FutureVoyagesViewModel` to consume these use-cases via DI gateways.
- Completed: expanded Phase 5 typed navigation coverage for chat/feedback argument routes by adding `InteractionRoutes` typed builders/patterns and migrating raw concatenation callsites in `VoyageDetails`, `CaptainVoyageDetails`, `PastVoyages`, `Dashboardscreen`, `StartedRequestTab`, `CaptainPastVoyages`, `VoyagersListScreen`, and `NavigationGraph`.
- Completed: extended Phase 6 with ViewModel behavior tests for touched booking flows (`BookVoyageViewModelTest`, `ConfirmBookedVoyageViewModelTest`, `CancelBookedVoyageViewModelTest`) plus shared test dispatcher rule (`MainDispatcherRule`) and coroutine-test dependency wiring.
- Completed: expanded Phase 5 typed navigation coverage to remaining account/auth argument routes by adding `AccountRoutes` typed builders/patterns and migrating `NavigationGraph`, `LoginScreen`, `SplashComposable`, `UserBasicInfoScreen`, `VerifyUserEmail`, `UserAccountInfoScreen`, `SettingsScreen`, `MenuOptions`, `CaptainMenuOptions`, `BusinessMenuOptions`, and `BusinessDetail` off raw route string interpolation.
- Completed: extended Phase 6 tests for payment confirmations with `PaymentViewModelTest` and `SponsorPaymentConfirmationViewModelTest`, and added `AccountRoutesTest` for new typed route builders.
- Started: Phase 7 hardening checklist with secure-storage/token invalidation edge-case cleanup by strengthening `SharedPrefManager.clearUserData()` to wipe both secure + legacy prefs and clear in-memory auth/session fields (`USER_ID`, `USER_NAME`, `JWT_TOKEN`) on logout/session reset.
- Completed: continued Phase 7 security hardening with a centralized logout/session-clear use-case (`ClearSessionUseCase`) wired via `coreModule` and used by `LoginViewModel`, `DashboardScreen` session-expiry dialog, `BusinessDashboard` session-expiry dialog, and role-switch logout flow (`SelectRole`) so session exits follow one clear path.
- Completed: expanded Phase 6 matrix with serialization/contract tests for payment payloads (`PaymentSerializationContractTest`) covering request serialization and response decoding contracts for `PaymentConfirmationRequest`, `VoyagePaymentResponse`, `PaymentSheetConfigResponse`, and `SponsorPayments`.
- Completed: finalized Phase 5 non-critical argument-route cleanup by removing remaining legacy commented raw-route snippets in `MenuOptions` and `SelectRole`; active argument-based navigation paths now use typed helpers (`VoyagerFlowRoutes`, `InteractionRoutes`, `AccountRoutes`).
- Completed: expanded Phase 7 session hardening with a small `SessionController` facade (`logoutAndResolveRoute`, `resolveRedirectRoute`) and migrated remaining logout/session-expiry callsites in menu/captain/business/voyager flows to route through this centralized clear+redirect path.
- Completed: extended Phase 6 API model contract coverage beyond payment with booking/confirm/cancel serialization tests in `VoyageBookingSerializationContractTest` (`BookVoyageRequest`, `BookVoyageResponse`, `ConfirmBookedVoyages`, `ConfirmBookedVoyageResponse`, `CancelBookedVoyages`, `CancelBookedVoyageResponse`).
- Started: next Phase 4 domain-hardening continuation on remaining non-`AppConstants` seams in chat flow by migrating voyager-list session ownership to scoped session source (`UserSessionStore`) in both presentation and data layers.
- Completed: removed chat voyager-list runtime global user-id dependency by updating `VoyagersRepository` and `VoyagersListScreen` to consume `UserSessionStore.currentUserId()` (and wiring `chatUserrolesModule` accordingly), eliminating direct `AppConstants.USER_ID` reads in this flow.
- Started: next Phase 4 signup/captain/business continuation to remove residual `AppConstants.USER_ID` usage from account/profile save paths and profile-fetch repositories.
- Completed: migrated signup/captain/business `USER_ID` seams to scoped session ownership (`UserSessionStore`) across `GetVoyagerProfileRepository`, `GetCaptainProfileRepository`, `GetCaptainDocsRepository`, `GetCaptainBoatRepository`, `GetBusinessProfileRepository`, `GetBusinessInfoRepository`, `CaptainDocsViewModel`, `CaptainBoatViewModel`, `BusinessInfoViewModel`, `UserAccountInfoScreen`, `CaptainAccountInfoScreen`, `BusinessAccountInfoScreen`, `AddBusinessLogo`, and `AddBusinessDescriptions` (with DI updates in `authModule`, `captainModule`, and `businessModule`).
- Completed: finalized AppConstants compatibility cleanup for session identity ownership by removing legacy runtime-global user/session mutations from `SharedPrefManager` (`saveLoginData`/`clearUserData`) and `SplashComposable`; session identity now resolves from persisted secure prefs through `UserSessionStore`.
- Completed: finalized JWT global cleanup by removing the last runtime-global token assignment (`AppConstants.JWT_TOKEN`) from `PasswordRepository`; token flow now relies on `TokenProvider`/secure pref storage paths only.
- Validation: IDE diagnostics are clean on all touched files after this continuation slice; terminal Gradle output remains partially unstable due recurring interactive prompt interruptions in this shell.
- Validation: full `phase0Safety` passed (`BUILD SUCCESSFUL`) after the SessionController facade + voyage booking contract-test continuation slice.
- Validation: `:app:compileDebugKotlin` and full `phase0Safety` passed (`EXIT_CODE=0`) after this chat session-store migration continuation slice.
- Validation: `:app:compileDebugKotlin` and full `phase0Safety` passed (`BUILD SUCCESSFUL`, `EXIT_CODE=0`) after this signup/captain/business `USER_ID` migration continuation slice.
- Validation: `:app:compileDebugKotlin` and full `phase0Safety` passed (`BUILD SUCCESSFUL`) after final `Prefmanager` + `SplashComposable` AppConstants cleanup slice.
- Validation: `:app:compileDebugKotlin` and full `phase0Safety` passed (`BUILD SUCCESSFUL`) after final JWT global cleanup slice.
- Validation: integrated pre-merge verification passed end-to-end (`scripts/backend-contract-smoke.ps1` + `scripts/pre-merge-safety.ps1 -BackendBaseUrl "http://127.0.0.1:5265"`) with backend runtime health (`/swagger/index.html` 200) and frontend phase0Safety gates green.
- Continued: Phase 4 domain-hardening slice for chat flow by introducing explicit chat use-cases (`FetchVoyagersUseCase`, `FollowVoyagerUseCase`, `ComplainVoyagerUseCase`, `ListenForMessagesUseCase`, `SendChatMessageUseCase`, `MarkMessagesAsReadUseCase`) and migrating `VoyagersListViewModel`, `FollowViewModel`, and `ChatViewModel` to use-case seams instead of direct repository calls.
- Completed: `chatUserrolesModule` DI wiring updated to provide use-cases through repository-backed gateway lambdas while keeping existing repository implementations intact.
- Completed: Phase 6 coverage extended for this slice via `ChatUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "*ChatUseCasesTest"` passed after chat use-case migration.
- Continued: next Phase 4 voyager-dashboard batch by introducing explicit interaction use-cases (`FetchVoyagerPastVoyagesUseCase`, `SubmitVoyagerFeedbackUseCase`, `FollowBusinessUseCase`, `UnFollowBusinessUseCase`, `FetchTravelNowVoyagesUseCase`) and migrating `VoyagerVoyagesViewModel`, `VoyagerFeedbackViewModel`, `VoyagerFollowBusinessViewModel`, and `TravelNowViewModel` off direct repository injection.
- Completed: `voyagerModule` DI wiring updated to provide new voyager interaction use-cases via repository-backed gateway lambdas while keeping existing repositories stable.
- Completed: Phase 6 coverage extended for this batch via `VoyagerDashboardInteractionUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "*VoyagerDashboardInteractionUseCasesTest"` passed after voyager-dashboard use-case migration.
- Continued: next Phase 4 captain-dashboard batch by introducing captain interaction use-cases (`AcceptVoyageUseCase`, `DeclineVoyageUseCase`, `StartVoyageUseCase`, `CompleteVoyageUseCase`, `CancelVoyageUseCase`, `FetchCaptainActiveVoyagesUseCase`, `SubmitCaptainFeedbackUseCase`, `FetchCaptainCompletedVoyagesUseCase`, `UpdateCaptainAvailabilityUseCase`) and migrating captain dashboard/availability viewmodels off direct repository injection.
- Completed: `captainModule` DI wiring updated to provide captain interaction use-cases via repository-backed gateway lambdas while keeping existing repositories stable.
- Completed: Phase 6 coverage extended for this batch via `CaptainDashboardUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "com.boatit.boatsharing.ui.captain.domain.usecase.CaptainDashboardUseCasesTest"` passed after captain-dashboard use-case migration.
- Continued: next Phase 4 captain-signup residual batch by introducing signup use-cases (`SaveCaptainProfileUseCase`, `SaveCaptainDocsUseCase`, `SaveCaptainBoatUseCase`, `FetchCaptainProfileUseCase`, `FetchCaptainDocsUseCase`, `FetchCaptainBoatUseCase`) and migrating captain signup profile/docs/boat viewmodels off direct repository injection.
- Completed: `captainModule` DI wiring updated to provide captain-signup use-cases via repository-backed gateway lambdas while keeping existing repositories stable.
- Completed: Phase 6 coverage extended for this batch via `CaptainSignupUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "com.boatit.boatsharing.ui.signup.captain.domain.usecase.CaptainSignupUseCasesTest"` passed after captain-signup use-case migration.
- Continued: next Phase 4 residual signup batch by introducing general/business signup use-cases and migrating remaining general/business signup viewmodels off direct repository injection.
- Completed: added general-signup use-case seam (`RegisterUserUseCase`, `VerifySignupEmailUseCase`, `RegisterPasswordUseCase`, `SaveVoyagerProfileUseCase`, `FetchVoyagerProfileUseCase`) and migrated general signup viewmodels (`RegistrationViewModel`, `VerifyEmailViewModel`, `PasswordViewModel`, `VoyagerProfileViewModel`, `GetVoyagerProfileViewModel`).
- Completed: added business-signup use-case seam (`SaveBusinessProfileUseCase`, `SaveBusinessInfoUseCase`, `SaveBusinessAboutUseCase`, `SaveBusinessLogoUseCase`, `SaveBusinessGalleryUseCase`, `FetchBusinessProfileUseCase`, `FetchBusinessInfoUseCase`) and migrated business signup viewmodels (`BusinessProfileViewModel`, `BusinessInfoViewModel`, `BusinessAboutViewModel`, `BusinessLogoViewModel`, `GetBusinessProfileViewModel`, `GetBusinessInfoViewModel`).
- Completed: `authModule` and `businessModule` DI wiring updated to provide these use-cases through repository-backed gateway lambdas while keeping repository implementations stable.
- Completed: Phase 6 coverage extended for this batch via `GeneralSignupUseCasesTest` and `BusinessSignupUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "com.boatit.boatsharing.ui.signup.general.domain.usecase.GeneralSignupUseCasesTest" --tests "com.boatit.boatsharing.ui.signup.business.domain.usecase.BusinessSignupUseCasesTest"` passed after general/business signup use-case migration.
- Continued: next Phase 4 residual non-signup auth/userroles batch by introducing explicit use-cases and migrating remaining login/forgot-password/role/fcm-token viewmodels off direct repository injection.
- Completed: added auth/user-role use-case seams (`LoginUserUseCase`, `SendForgotPasswordUseCase`, `AssignUserRoleUseCase`, `UpdateDeviceTokenUseCase`) and migrated `LoginViewModel`, `ForgotPassViewModel`, `RoleViewModel`, and `FCMTokenViewModel` to consume use-cases.
- Completed: `authModule` and `chatUserrolesModule` DI wiring updated to provide these use-cases through repository-backed gateway lambdas while keeping repository implementations stable.
- Completed: Phase 6 coverage extended for this batch via `LoginUseCasesTest`, `ForgotPasswordUseCasesTest`, and `UserRoleUseCasesTest`.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "com.boatit.boatsharing.ui.login.domain.usecase.LoginUseCasesTest" --tests "com.boatit.boatsharing.ui.forgotpassword.domain.usecase.ForgotPasswordUseCasesTest" --tests "com.boatit.boatsharing.ui.userroles.domain.usecase.UserRoleUseCasesTest"` passed after auth/userroles use-case migration.
- Continued: next Phase 4 residual non-signup business/voyager batch by introducing explicit use-cases and migrating remaining business/voyager viewmodels off direct repository injection.
- Completed: added business dashboard use-case seam (`FetchBusinessDashboardProfileUseCase`, `FetchBusinessDocksUseCase`, `SaveBusinessDashboardProfileUseCase`, `DeleteBusinessDashboardImageUseCase`) and migrated `GetBusinessViewModel` + `BusinessDashViewModel` to consume use-cases.
- Completed: extended voyager use-case seams for remaining residuals (`FetchBusinessRelationshipsUseCase`, `FetchFollowedVoyagersUseCase`, `FetchSponsorPaymentsUseCase`, `FetchPaymentSheetConfigUseCase`, `FetchSponsorPaymentSheetConfigUseCase`, `DeclineSponsorPaymentUseCase`) and migrated `FetchBusinessViewModel`, `FollowedVoyagerViewModel`, `SponcerVoyagesViewModel`, `PaymentSheetConfigViewModel`, and `SponsorPaymentSheetConfigViewModel`.
- Completed: `businessModule` and `voyagerModule` DI wiring updated to provide these use-cases through repository-backed gateway lambdas while keeping repository implementations stable.
- Completed: Phase 6 coverage extended for this batch via `BusinessDashboardUseCasesTest` and `VoyagePaymentAndStatusUseCasesTest`, plus extended `VoyagerDashboardInteractionUseCasesTest` coverage for new voyager seams.
- Validation: `:app:compileDebugKotlin` and targeted `:app:testDebugUnitTest --tests "com.boatit.boatsharing.ui.business.domain.usecase.BusinessDashboardUseCasesTest" --tests "com.boatit.boatsharing.ui.voyager.dashboard.domain.usecase.VoyagerDashboardInteractionUseCasesTest" --tests "com.boatit.boatsharing.ui.voyager.dashboard.domain.usecase.VoyagePaymentAndStatusUseCasesTest"` passed after business/voyager residual use-case migration.
- Continued: strict residual seam audit found remaining direct repository injections in `BusinessDashboardViewModel`, `CreateVoyageSponsorViewModel`, `NearByVoyagesViewModel`, `FindBoatViewModel`, and `CalculateFairViewModel`.
- Completed: migrated those residual viewmodels to explicit use-case seams (`FetchBusinessDashboardProfileUseCase`, `FetchBusinessDocksUseCase`, `SaveBusinessDashboardProfileUseCase`, `SaveBusinessGalleryUseCase`, `FetchActiveVoyagersUseCase`, `FetchNearbyPlacesUseCase`, `FetchVoyageCategoriesUseCase`, `FindBoatUseCase`, `CalculateVoyageFareUseCase`) and removed direct repository dependencies from these presentation paths.
- Completed: updated `voyagerModule` DI to provide new use-cases via repository-backed gateway lambdas (`VoyagersRepository`, `FetchNearByVoyagesRepo`, `FetchCategoryRepo`, `FindBoatRepo`, `CalculateFairRepository`), while preserving repository implementations.
- Completed: extended Phase 6 coverage for this final seam closure by expanding `VoyagerDashboardInteractionUseCasesTest` and `VoyageBookingUseCasesTest` for the newly added use-case wrappers.

Refactor closure snapshot (as of 2026-04-05):
- Completed: all currently identified `AppConstants` references under `app/src/**` are removed for the active migration scope.
- Completed: final residual cleanup was validated across `MapPickerScreen`, `ApiConstants`, and `AddBusinessLogo`; obsolete `utils/Constants.kt` removed.
- Validation: post-cleanup `:app:compileDebugKotlin` and full `phase0Safety` passed (`EXIT_CODE=0`).
- Ongoing: continue Phase 4+ domain hardening on non-`AppConstants` seams (use-case boundaries, deterministic state ownership), while keeping compile/safety gates green.

Phase completion snapshot (latest):
- Completed (explicitly done): 2 phases (`Phase 0`, `Phase 2`).
- In progress: 6 phases (`Phase 1`, `Phase 3`, `Phase 4`, `Phase 5`, `Phase 6`, `Phase 7`).
- Not started: 0 phases.

## 11) Final Closure Checklist (Document Complete)

This plan document is now in completed format for execution tracking.

Closure checklist:
- Architecture direction documented (feature-first, phased, incremental migration).
- Backend-first safety gates documented and automated.
- Phase-wise goals, exit criteria, and execution order captured.
- Risks and mitigations captured.
- Refactor progress log and validation trail captured.
- Current completion snapshot and active next focus captured.

## 12) Operating Mode After Plan Completion

From this point onward, this file should be treated as a living execution ledger:

- For each migration slice, append only delta updates under the latest phase/status block.
- For each validation run, append one concise result line with task name and outcome.
- Do not rewrite prior history unless correcting factual errors.
- Keep phase snapshot synchronized when a phase moves from `In progress` to `Completed`.

## Note

`BusinessDashboard` refactor is the pilot pattern reference. Reuse it where it improves state clarity, while enforcing unified `UiState` / `UiEvent` / `UiEffect` contracts.
