# **LOW-LEVEL REQUIREMENTS (LLRs)**
## **Centralized Session Management System**
### **DO-178C DAL D Compliance**

**Document ID:** LLR-SESSION-001  
**Version:** 1.0  
**Date:** [Current Date]  
**Author:** [Author Name]  
**Reviewer:** [Reviewer Name]  
**Approver:** [Approver Name]

---

## **1. INTRODUCTION**

### **1.1 Purpose**
This Low-Level Requirements (LLR) document defines the detailed implementation requirements for the Centralized Session Management System. These requirements provide specific technical specifications for implementing the high-level requirements defined in HLRs-SessionManagement.md.

### **1.2 Scope**
The Low-Level Requirements encompass:
- Detailed logging implementation specifications
- Performance monitoring implementation details
- Configuration parameter management specifics
- Security implementation mechanisms
- Event broadcasting technical details
- Timeout implementation specifics
- Network retry algorithm specifications

### **1.3 Applicable Documents**
- HLRs-SessionManagement.md - High-Level Requirements Document
- SRD-SessionManagement.md - Systems Requirements Document
- DO-178C - Software Considerations in Airborne Systems and Equipment Certification
- Android Development Guidelines
- Kotlin Programming Language Documentation

### **1.4 Definitions and Acronyms**
- **DAL:** Design Assurance Level
- **HLR:** High-Level Requirement
- **LLR:** Low-Level Requirement
- **SRD:** Systems Requirements Document
- **Log:** Android Logging Framework
- **SharedFlow:** Kotlin reactive stream for event broadcasting
- **EARS:** Easy Approach to Requirements Syntax

### **1.5 EARS Compliance**
This document follows the Easy Approach to Requirements Syntax (EARS) methodology to ensure requirements are:
- **Clear and unambiguous:** Each requirement uses simple, declarative language
- **Testable:** Requirements can be verified through analysis, testing, or review
- **Atomic:** Each requirement addresses a single implementation concern
- **Consistent:** Requirements follow consistent patterns and terminology

**EARS Templates Used:**
- **Ubiquitous Requirements:** "The function SHALL [implementation detail]"
- **Event-Driven Requirements:** "When [trigger], the function SHALL [implementation response]"
- **State-Driven Requirements:** "While [state], the function SHALL [implementation behavior]"
- **Unwanted Behavior Requirements:** "The function SHALL NOT [prohibited implementation]"

---

## **2. LOW-LEVEL REQUIREMENTS**

### **2.1 Logging Implementation Details**

#### **LLR-1.1.1: Event Logging Implementation**
**Requirement:** The function `Log.i()` SHALL log all session events using Android's logging framework.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides audit trail for debugging and compliance using Android's optimized logging system.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()` (scattered throughout SessionManager)

#### **LLR-1.1.2: Timestamp Logging Implementation**
**Requirement:** The function `Log.i()` SHALL include timestamps in all session event logs using `System.currentTimeMillis()`.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides chronological audit trail for debugging and compliance.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()` (scattered throughout SessionManager)

#### **LLR-1.1.3: Event Context Logging Implementation**
**Requirement:** The function `Log.i()` SHALL include relevant context (user ID, session state, error details) in session event logs.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides sufficient information for debugging and analysis.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()` (scattered throughout SessionManager)

#### **LLR-1.2.1: Log Level Assignment Implementation**
**Requirement:** The function `Log.i()`, `Log.w()`, `Log.e()` SHALL assign appropriate log levels to session events based on severity.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables proper log filtering and prioritization for monitoring.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.2.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()`, `Log.w()`, `Log.e()` (scattered throughout SessionManager)

#### **LLR-1.2.2: Log Level Standards Implementation**
**Requirement:** The function `Log.i()`, `Log.w()`, `Log.e()` SHALL use standard Android log levels (INFO, WARNING, ERROR, CRITICAL).
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures consistent log level usage across the system.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.2.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()`, `Log.w()`, `Log.e()` (scattered throughout SessionManager)

#### **LLR-1.2.3: Log Level Validation Implementation**
**Requirement:** The function `Log.i()`, `Log.w()`, `Log.e()` SHALL validate log level assignments are appropriate for event severity.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures critical events are properly prioritized in logs.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-8.2.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.i()`, `Log.w()`, `Log.e()` (scattered throughout SessionManager)

---

### **2.2 Performance Monitoring Implementation**

#### **LLR-2.1.1: Performance Monitoring Implementation**
**Requirement:** The function `Log.d()` SHALL monitor session operation performance using debug-level logging.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables tracking and optimization of session performance without impacting production logs.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-9.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.d()` (scattered throughout SessionManager)

#### **LLR-2.1.2: Performance Degradation Handling Implementation**
**Requirement:** When session operation performance degrades, the function `Log.w()` SHALL log performance issues with warning level.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables identification and resolution of performance problems.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-9.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.w()` (scattered throughout SessionManager)

---

### **2.3 Configuration Implementation Details**

#### **LLR-3.1.1: Configuration Parameter Definition Implementation**
**Requirement:** The function `SessionManager` SHALL define configurable session parameters as constructor parameters or configuration objects.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides clear specification of what can be configured at runtime.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-10.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` constructor

#### **LLR-3.1.2: Configuration Validation Implementation**
**Requirement:** The function `SessionManager` SHALL validate external configuration parameters using range checks and type validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Prevents invalid configurations that could compromise system functionality.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-10.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` constructor

---

### **2.4 Security Implementation Details**

#### **LLR-4.1.1: Data Encryption in Transit Implementation**
**Requirement:** The function `intercept()` SHALL encrypt user data in transit using HTTPS/TLS protocols.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Protects sensitive user data from unauthorized access during transmission.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-12.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **LLR-4.1.2: Encryption Key Management Implementation**
**Requirement:** The function `saveLoginData(userData: UserData)` SHALL implement proper encryption key management using Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures encryption keys are properly secured and managed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-12.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `saveLoginData(userData: UserData)`

#### **LLR-4.2.1: Access Control Policy Definition Implementation**
**Requirement:** The function `SessionManager` SHALL define access control policies as data classes or enums for session data access.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Establishes clear rules for who can access what session data.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-12.2.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` (access control logic)

#### **LLR-4.2.2: Access Control Enforcement Implementation**
**Requirement:** The function `SessionManager` SHALL enforce access control policies using role-based checks and session state validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures only authorized users can access sensitive data.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-12.2.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` (access control logic)

#### **LLR-4.2.3: Access Control Monitoring Implementation**
**Requirement:** The function `Log.w()` SHALL monitor access control violations using warning-level logging.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables detection and response to unauthorized access attempts.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-12.2.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `Log.w()` (scattered throughout SessionManager)

---

### **2.5 Event Broadcasting Implementation**

#### **LLR-5.1.1: Event Delivery Implementation**
**Requirement:** The function `GlobalSessionHandler()` SHALL deliver events to all subscribed components using Kotlin Flow's `collectAsState()`.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures all components receive critical session events.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-6.2.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

#### **LLR-5.1.2: Event Delivery Confirmation Implementation**
**Requirement:** The function `GlobalSessionHandler()` SHALL confirm event delivery to subscribed components using Flow's completion callbacks.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures events are successfully delivered and processed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-6.2.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

#### **LLR-5.2.1: Event Delivery Guarantee Implementation**
**Requirement:** The function `_sessionEvents.emit(event)` SHALL implement a mechanism to guarantee event delivery using SharedFlow's replay cache.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures no critical session events are lost.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-6.3.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `_sessionEvents.emit(event)` (via SharedFlow)

#### **LLR-5.2.2: Event Retry on Failure Implementation**
**Requirement:** When event delivery fails, the function `_sessionEvents.emit(event)` SHALL retry delivery using SharedFlow's built-in retry mechanisms.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures events are eventually delivered even after temporary failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-6.3.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `_sessionEvents.emit(event)` (via SharedFlow)

#### **LLR-5.2.3: Event Persistence Implementation**
**Requirement:** The function `_sessionEvents.emit(event)` SHALL persist critical events until delivery is confirmed using SharedFlow's replay mechanism.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Prevents loss of critical events during system failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-6.3.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `_sessionEvents.emit(event)` (via SharedFlow)

---

### **2.6 Timeout Implementation Details**

#### **LLR-6.1.1: User Warning Display Implementation**
**Requirement:** When session timeout approaches, the function `GlobalSessionHandler()` SHALL display warnings to users using Compose dialogs.
**EARS Template:** Event-Driven Requirement
**Rationale:** Gives users opportunity to extend session before automatic logout.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-7.2.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

#### **LLR-6.1.2: Warning Timing Configuration Implementation**
**Requirement:** The function `startSessionMonitoring()` SHALL configure warning timing using configurable constants (e.g., 5 minutes before timeout).
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides appropriate advance notice for users to take action.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-7.2.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `startSessionMonitoring()`

#### **LLR-6.2.1: Memory Cleanup Implementation**
**Requirement:** When session timeout occurs, the function `performLogout()` SHALL clean up session data from memory using null assignments and state resets.
**EARS Template:** Event-Driven Requirement
**Rationale:** Frees memory resources and prevents data leakage.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-7.4.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `performLogout()` (private)

#### **LLR-6.2.2: Storage Cleanup Implementation**
**Requirement:** When session timeout occurs, the function `performLogout()` SHALL clean up session data from storage using `tokenProvider.clearTokens()`.
**EARS Template:** Event-Driven Requirement
**Rationale:** Removes persistent session data to prevent unauthorized access.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-7.4.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `performLogout()` (private)

---

### **2.7 Network Retry Algorithm Implementation**

#### **LLR-7.1.1: Exponential Backoff Implementation**
**Requirement:** When retrying server errors, the function `getRetryDelay(attempt: Int)` SHALL use exponential backoff delays using the formula `(1000L * Math.pow(2.0, attempt.toDouble())).toLong()`.
**EARS Template:** Event-Driven Requirement
**Rationale:** Handles temporary server issues with intelligent retry strategy.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `getRetryDelay(attempt: Int)`

#### **LLR-7.1.2: Linear Backoff Implementation**
**Requirement:** When retrying timeout errors, the function `getRetryDelay(attempt: Int)` SHALL use linear backoff delays using the formula `(2000L * (attempt + 1)).toLong()`.
**EARS Template:** Event-Driven Requirement
**Rationale:** Handles network timeout issues with appropriate retry strategy.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** HLR-3.2.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `getRetryDelay(attempt: Int)` (needs linear implementation)

---

## **3. REQUIREMENT DEPENDENCIES**

| LLR ID | Depends On | Dependency Type |
|--------|------------|-----------------|
| LLR-1.1.2 | LLR-1.1.1 | Implementation |
| LLR-1.1.3 | LLR-1.1.1 | Implementation |
| LLR-1.2.1 | LLR-1.1.1 | Implementation |
| LLR-1.2.2 | LLR-1.2.1 | Implementation |
| LLR-1.2.3 | LLR-1.2.1 | Implementation |
| LLR-2.1.2 | LLR-2.1.1 | Implementation |
| LLR-3.1.2 | LLR-3.1.1 | Implementation |
| LLR-4.1.2 | LLR-4.1.1 | Implementation |
| LLR-4.2.1 | LLR-4.1.1 | Implementation |
| LLR-4.2.2 | LLR-4.2.1 | Implementation |
| LLR-4.2.3 | LLR-4.2.1 | Implementation |
| LLR-5.1.2 | LLR-5.1.1 | Implementation |
| LLR-5.2.1 | LLR-5.1.1 | Implementation |
| LLR-5.2.2 | LLR-5.2.1 | Implementation |
| LLR-5.2.3 | LLR-5.2.1 | Implementation |
| LLR-6.1.2 | LLR-6.1.1 | Implementation |
| LLR-6.2.1 | LLR-6.1.1 | Implementation |
| LLR-6.2.2 | LLR-6.2.1 | Implementation |
| LLR-7.1.2 | LLR-7.1.1 | Implementation |

---

## **4. VERIFICATION AND VALIDATION**

### **4.1 Verification Methods**
- **Analysis:** Code analysis, design analysis, implementation analysis
- **Testing:** Unit testing, integration testing, implementation testing
- **Review:** Code review, implementation review, technical review

### **4.2 Validation Criteria**
Each low-level requirement shall be considered satisfied when:
1. Implementation code demonstrates compliance with the requirement
2. Unit tests verify the specific implementation behavior
3. Code review confirms proper implementation approach
4. Integration testing validates implementation works correctly
5. Performance testing confirms implementation meets performance criteria
6. Documentation accurately reflects the implementation

---

## **5. CHANGE CONTROL**

Any changes to low-level requirements shall follow the established change control process:
1. Change request submission
2. Impact analysis on implementation
3. Review and approval
4. Implementation update
5. Verification
6. Documentation update

---

## **6. APPENDICES**

### **Appendix A: Implementation Glossary**
- **Log.i():** Android INFO level logging
- **Log.w():** Android WARNING level logging
- **Log.e():** Android ERROR level logging
- **Log.d():** Android DEBUG level logging
- **SharedFlow:** Kotlin reactive stream with replay capability
- **Exponential Backoff:** Increasing delay between retry attempts
- **Linear Backoff:** Constant increment delay between retry attempts

### **Appendix B: References**
- Android Logging Documentation
- Kotlin Flow Documentation
- Android Keystore Documentation
- Network Retry Best Practices

---

**Document Control:**
- **Status:** Draft
- **Distribution:** Development Team, QA Team, Technical Leads
- **Next Review Date:** [Date + 30 days]
- **Approval Required:** Technical Lead, Senior Developer, QA Lead
