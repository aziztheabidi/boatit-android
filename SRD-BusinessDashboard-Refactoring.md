# **SYSTEMS REQUIREMENTS DOCUMENT (SRD) - CULLED & REFINED**
## **BusinessDashboard Refactoring System**
### **DO-178C DAL D Compliance**

**Document ID:** SRD-BUSINESS-DASHBOARD-001  
**Version:** 2.0 (Culled & Refined)  
**Date:** [Current Date]  
**Author:** [Author Name]  
**Reviewer:** [Reviewer Name]  
**Approver:** [Approver Name]

---

## **1. INTRODUCTION**

### **1.1 Purpose**
This Systems Requirements Document (SRD) defines the **essential** functional and non-functional requirements for refactoring the BusinessDashboard composable from a monolithic 1,220-line file into a modular, maintainable, and scalable architecture following MVVM patterns and Jetpack Compose best practices.

### **1.2 Scope**
The BusinessDashboard Refactoring System encompasses:
- **Essential**: Decomposition of monolithic composable into focused components
- **Essential**: Separation of business logic from UI logic
- **Essential**: Implementation of proper state management
- **Essential**: Integration with centralized session management
- **Essential**: Basic error handling and retry mechanisms
- **Essential**: Performance optimization and maintainability improvements

### **1.3 Culling Summary**
**CULLED Requirements (Overly Complex/Unnecessary):**
- ❌ **SR-3.1.2**: Image reordering with drag-and-drop (unnecessary complexity)
- ❌ **SR-3.1.3**: Complex image storage security (over-engineered)
- ❌ **SR-4.1.1**: Location data caching (unnecessary optimization)
- ❌ **SR-5.1.2**: Overlapping hours detection (overly complex validation)
- ❌ **SR-6.1.2**: Complex memory management (Compose handles automatically)

**SIMPLIFIED Requirements:**
- 🔄 **SR-2.1.1**: Basic retry logic (3 attempts, existing Ktor retry)
- 🔄 **SR-7.1.1**: Component size limit (200 → 300 lines)
- 🔄 **SR-7.1.3**: Memory usage (remove specific limit, focus on efficiency)

---

## **2. SYSTEM REQUIREMENTS**

### **2.1 Functional Requirements**

#### **SR-1.1.1: Monolithic Composable Decomposition** ✅ **ESSENTIAL**
**Requirement:** The system SHALL decompose the monolithic BusinessDashboard composable into focused, single-responsibility components.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Improves maintainability, testability, and code reusability by separating concerns.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Break 1,220-line BusinessDashboard.kt into: BusinessProfileComposable (business info), BusinessGalleryComposable (image management), BusinessLocationComposable (location/dock), BusinessHoursComposable (hours editing), BusinessActionsComposable (save actions). Each component shall have a single, well-defined responsibility and shall not exceed 300 lines of code.

#### **SR-1.1.2: State Management Separation** ✅ **ESSENTIAL**
**Requirement:** The system SHALL separate UI state from business state using proper ViewModel architecture.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures proper separation of concerns and enables better testing and maintainability.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** UI state (expanded dropdowns, editing modes, focus management) shall remain in composables using `remember`. Business state (business data, form data, loading states, error states) shall be managed in BusinessDashboardViewModel using `StateFlow` and `MutableStateFlow`.

#### **SR-1.1.3: Business Logic Extraction** ✅ **ESSENTIAL**
**Requirement:** The system SHALL extract business logic from UI components into dedicated ViewModel and service classes.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Improves testability and maintains separation of concerns between UI and business logic.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Extract form validation, data processing, network call handling, error handling, and navigation logic into BusinessDashboardViewModel and BusinessValidationService. UI components shall only handle user interactions and display logic.

#### **SR-1.1.4: Session Management Integration** ✅ **ESSENTIAL**
**Requirement:** The system SHALL integrate with the centralized session management system for authentication and session events.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures consistent session handling and automatic logout on session expiration.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Subscribe to SessionEvent.LogoutRequired, SessionEvent.SessionExpired, SessionEvent.TokenRefreshFailed, and SessionEvent.AccountDeactivated events using GlobalSessionHandler. Handle session timeout warnings and automatically navigate to login screen on session events.

---

#### **SR-2.1.1: Basic Error Handling Implementation** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic error handling with retry mechanisms for network operations.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides robust error recovery and improves user experience during network failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Detect network errors, display appropriate error messages to users, implement basic retry mechanisms (3 attempts using existing Ktor HttpRequestRetry plugin), and handle different error types (network timeout, server errors, client errors). Provide specific guidance for different error scenarios.

#### **SR-2.1.2: Loading State Management** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement proper loading state management for all asynchronous operations.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides clear user feedback during operations and prevents multiple simultaneous requests.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Show loading indicators during network operations, disable UI interactions during loading, prevent multiple simultaneous requests, and provide loading feedback for different operations (business data fetch, image upload, profile save). Use a single loading state managed in ViewModel rather than multiple loading flags.

#### **SR-2.1.3: Basic Form Validation Implementation** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic form validation with real-time feedback.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures data integrity and provides immediate user feedback for validation errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Validate business name (not empty, length limits), validate business description (not empty, character limits), validate location selections (zone, shore, island must be selected), validate business hours (start time before end time), and validate dock information (name, address, description when dock is enabled). Show validation errors immediately as user types or selects options, highlight invalid fields with error colors, and prevent form submission until all validation passes.

---

#### **SR-3.1.1: Basic Image Upload Management** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic image upload management with progress tracking and error handling.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides reliable image upload functionality with proper user feedback and error recovery.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Support multiple image selection, limit uploads to 6 images maximum, show upload progress indicators, handle upload failures with retry options, validate image formats and sizes, and provide clear error messages for upload failures. Use existing BusinessLogoViewModel for upload state management and integrate with session management system for authentication.

#### **SR-3.1.2: Basic Gallery Management** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic gallery management with add and remove capabilities.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides essential gallery management functionality for business images.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Display existing images in grid layout, allow image removal with confirmation dialog, show image previews with proper aspect ratios, and handle both local and remote images. Users can add up to 6 images total and remove any existing image with confirmation dialog. Use ImageGridWithAddOption composable for gallery display.

---

#### **SR-4.1.1: Basic Location Management** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic location management with zone, shore, and island selection.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides essential location management for business operations and dock placement.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Fetch available zones, shores, and islands from server, display location options in dropdown menus, validate location selections, and update business location data. Users can select from available zones, shores, and islands with proper validation that all three are selected before saving. Use BusinessLocationViewModel for location state management.

#### **SR-4.1.2: Dock Management** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement dock management with enable/disable functionality and detailed dock information.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides complete dock management for businesses offering dock services.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Enable/disable dock services with toggle switch, collect dock information (name, address, description) when enabled, validate dock information when dock is enabled, and save dock configuration with business profile. Use AddDockSection composable for dock management and validate dock information before saving.

#### **SR-4.1.3: Address Management** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement address management with map integration and validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides accurate address management with map-based location selection.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Display current business address, allow address editing through map picker, validate address format and completeness, and update address in business profile. Use EditableLocationSection composable for address management and integrate with map picker navigation.

---

#### **SR-5.1.1: Business Hours Management** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement comprehensive business hours management with editing capabilities.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides complete business hours management for accurate business operation information.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Display current business hours in readable format, allow hours editing through modal bottom sheet, validate business hours (start time before end time), and save updated hours with business profile. Users can edit hours for each day of the week, select start and end times from dropdown menus, and save changes with validation.

#### **SR-5.1.2: Basic Hours Validation** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic business hours validation with user feedback.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures business hours are logical and prevents basic conflicts in operating schedules.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Validate that start time is before end time for each day, ensure at least one day has valid hours, and provide clear error messages for validation failures. Provide real-time feedback during editing.

#### **SR-5.1.3: Hours Persistence** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement business hours persistence with proper data synchronization.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures business hours are properly saved and synchronized across the application.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Save hours changes to server, update local state with saved hours, handle save failures with retry options, and synchronize hours across different business views. Hours are saved immediately when user confirms changes, local state is updated with server response, and hours are refreshed from server on app restart.

---

### **2.2 Non-Functional Requirements**

#### **SR-6.1.1: Component Size Limits** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL limit individual composable components to maximum 300 lines of code.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures components are focused, maintainable, and testable.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Individual composable components shall not exceed 300 lines of code including function signature, parameters, state management, UI composition, and event handling. Components exceeding 300 lines shall be further decomposed into smaller, focused components.

#### **SR-6.1.2: State Update Performance** ✅ **ESSENTIAL**
**Requirement:** The system SHALL complete state updates within 16ms for smooth UI performance.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures smooth 60fps UI performance without frame drops.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** State updates (form field updates, dropdown selections, image upload progress, loading state changes, error state updates) shall complete in ≤16ms to maintain 60fps performance. Use efficient state management patterns, minimize unnecessary recompositions, and implement proper state hoisting.

#### **SR-6.1.3: Efficient Memory Usage** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement efficient memory usage for BusinessDashboard components.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures efficient memory usage and prevents memory-related crashes.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Implement efficient memory usage through proper Compose lifecycle management, use efficient state management, and properly dispose of resources when composables are removed. Monitor memory usage using Android Studio Memory Profiler.

---

#### **SR-7.1.1: Basic Error Recovery Implementation** 🔄 **SIMPLIFIED**
**Requirement:** The system SHALL implement basic error recovery with retry mechanisms for all network operations.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides robust error handling and improves user experience during network failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Detect network errors, implement basic retry mechanisms (3 attempts using existing Ktor retry), handle different error types (timeout, server error, client error), and provide user feedback for error states. Provide specific guidance for different error scenarios.

#### **SR-7.1.2: Data Validation Implementation** ✅ **ESSENTIAL**
**Requirement:** The system SHALL implement comprehensive data validation with real-time feedback for all user inputs.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures data integrity and provides immediate user feedback for validation errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Validate business name, business description, location selections, business hours, and dock information. Show validation errors immediately as user types or selects options, highlight invalid fields with error colors, and prevent form submission until all validation passes. Use BusinessValidationService for validation logic.

#### **SR-7.1.3: Session Integration Implementation** ✅ **ESSENTIAL**
**Requirement:** The system SHALL integrate with centralized session management for authentication and session events.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures consistent session handling and automatic logout on session expiration.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Elaboration:** Subscribe to SessionEvent.LogoutRequired, SessionEvent.SessionExpired, SessionEvent.TokenRefreshFailed, and SessionEvent.AccountDeactivated events using GlobalSessionHandler. Handle session timeout warnings and automatically navigate to login screen on session events.

---

## **3. REQUIREMENT DEPENDENCIES**

| SR ID | Depends On | Dependency Type |
|-------|------------|-----------------|
| SR-1.1.2 | SR-1.1.1 | Functional |
| SR-1.1.3 | SR-1.1.1 | Functional |
| SR-1.1.4 | SR-1.1.1 | Functional |
| SR-2.1.2 | SR-2.1.1 | Functional |
| SR-2.1.3 | SR-2.1.1 | Functional |
| SR-3.1.2 | SR-3.1.1 | Functional |
| SR-4.1.2 | SR-4.1.1 | Functional |
| SR-4.1.3 | SR-4.1.1 | Functional |
| SR-5.1.2 | SR-5.1.1 | Functional |
| SR-5.1.3 | SR-5.1.1 | Functional |
| SR-6.1.2 | SR-6.1.1 | Performance |
| SR-6.1.3 | SR-6.1.1 | Performance |
| SR-7.1.2 | SR-7.1.1 | Quality |
| SR-7.1.3 | SR-7.1.1 | Quality |

---

## **4. IMPLEMENTATION PRIORITY**

### **Phase 1: Core Architecture (Essential)**
1. **SR-1.1.1**: Monolithic Composable Decomposition
2. **SR-1.1.2**: State Management Separation
3. **SR-1.1.3**: Business Logic Extraction
4. **SR-1.1.4**: Session Management Integration

### **Phase 2: Error Handling & Validation (Essential)**
5. **SR-2.1.1**: Basic Error Handling Implementation
6. **SR-2.1.2**: Loading State Management
7. **SR-2.1.3**: Basic Form Validation Implementation
8. **SR-7.1.2**: Data Validation Implementation

### **Phase 3: Feature Implementation (Essential)**
9. **SR-3.1.1**: Basic Image Upload Management
10. **SR-3.1.2**: Basic Gallery Management
11. **SR-4.1.1**: Basic Location Management
12. **SR-4.1.2**: Dock Management
13. **SR-4.1.3**: Address Management
14. **SR-5.1.1**: Business Hours Management
15. **SR-5.1.2**: Basic Hours Validation
16. **SR-5.1.3**: Hours Persistence

### **Phase 4: Performance & Quality (Essential)**
17. **SR-6.1.1**: Component Size Limits
18. **SR-6.1.2**: State Update Performance
19. **SR-6.1.3**: Efficient Memory Usage
20. **SR-7.1.1**: Basic Error Recovery Implementation
21. **SR-7.1.3**: Session Integration Implementation

---

## **5. ESTIMATED EFFORT**

- **Phase 1 (Core Architecture)**: 1-2 weeks
- **Phase 2 (Error Handling & Validation)**: 1 week
- **Phase 3 (Feature Implementation)**: 2-3 weeks
- **Phase 4 (Performance & Quality)**: 1 week

**Total estimated effort: 5-7 weeks** (reduced from original 7-8 weeks due to culling)

---

## **6. VERIFICATION AND VALIDATION**

### **6.1 Verification Methods**
- **Analysis:** Requirements analysis, design analysis, code analysis
- **Testing:** Unit testing, integration testing, system testing
- **Review:** Peer review, inspection, walkthrough

### **6.2 Validation Criteria**
Each system requirement shall be considered satisfied when:
1. All associated implementation components are verified
2. Verification evidence demonstrates compliance
3. Integration testing confirms proper system behavior
4. Performance testing validates constraint compliance
5. Code review confirms proper architecture implementation
6. Documentation is complete and accurate

---

## **7. CHANGE CONTROL**

Any changes to system requirements shall follow the established change control process:
1. Change request submission
2. Impact analysis
3. Review and approval
4. Implementation
5. Verification
6. Documentation update

---

## **8. APPENDICES**

### **Appendix A: Culling Rationale**
- **Overly Complex**: Requirements that add unnecessary complexity without significant value
- **Over-Engineered**: Requirements that exceed the actual needs of the business dashboard
- **Unnecessary Optimization**: Requirements that optimize for scenarios that don't occur in practice
- **Essential**: Requirements that directly address the core problems identified in the code review

### **Appendix B: References**
- Android Jetpack Compose Guidelines
- MVVM Architecture Best Practices
- Kotlin Programming Language Documentation
- DO-178C Software Considerations in Airborne Systems

---

**Document Control:**
- **Status:** Draft (Culled & Refined)
- **Distribution:** Development Team, QA Team, Management
- **Next Review Date:** [Date + 30 days]
- **Approval Required:** Technical Lead, QA Lead, Project Manager
