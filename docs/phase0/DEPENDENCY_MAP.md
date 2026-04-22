# Phase 0 Dependency Map (High Level)

## Frontend (james_boat)

- App module: `james_boat/app`
- DI entry: `james_boat/app/src/main/java/com/boatit/boatsharing/network/di/Modules.kt`
- Navigation entry: `james_boat/app/src/main/java/com/boatit/boatsharing/routes/NavigationGraph.kt`
- Runtime shared state hotspot: `james_boat/app/src/main/java/com/boatit/boatsharing/utils/Constants.kt`

## Backend (peter)

- Solution: `peter/Boat_Sharing.sln`
- API project: `peter/Project.WebApi`
- Controller routes base: `api/[controller]`

## Critical Mobile Contract Paths (must stay stable)

- `POST /api/Account/Login`
- `POST /api/Account/RefreshToken`
- `POST /api/Voyage/FindBoat`
- `POST /api/Voyage/Book`
- `POST /api/Voyage/Confirm`
- `GET /api/Business/Get`
- `POST /api/Business/Save`

## Verification Scripts

- Backend contract smoke: `scripts/backend-contract-smoke.ps1`
- Combined pre-merge safety: `scripts/pre-merge-safety.ps1`
- PR checklist template: `scripts/PHASE0_PR_CHECKLIST.md`
