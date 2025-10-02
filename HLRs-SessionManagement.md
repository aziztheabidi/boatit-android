# **HIGH-LEVEL REQUIREMENTS (HLRs)**
## **Centralized Session Management System**
### **DO-178C DAL D Compliance**

**Document ID:** HLR-SESSION-001  
**Version:** 1.0  
**Date:** [Current Date]  
**Author:** [Author Name]  
**Reviewer:** [Reviewer Name]  
**Approver:** [Approver Name]

---

## **1. INTRODUCTION**

### **1.1 Purpose**
This High-Level Requirements (HLR) document defines the high-level functional and non-functional requirements for the Centralized Session Management System. These requirements provide the foundation for detailed system requirements and implementation.

### **1.2 Scope**
The Centralized Session Management System encompasses:
- User session lifecycle management
- Authentication token handling
- Network error recovery
- Session state persistence
- Security event handling
- Global event broadcasting
- Performance and resource management

### **1.3 Applicable Documents**
- SRD-SessionManagement.md - Systems Requirements Document
- DO-178C - Software Considerations in Airborne Systems and Equipment Certification
- IEEE 830-1998 - IEEE Recommended Practice for Software Requirements Specifications
- Android Security Best Practices
- OWASP Mobile Security Guidelines

### **1.4 Definitions and Acronyms**
- **DAL:** Design Assurance Level
- **HLR:** High-Level Requirement
- **LLR:** Low-Level Requirement
- **SRD:** Systems Requirements Document
- **JWT:** JSON Web Token
- **API:** Application Programming Interface
- **HTTP:** Hypertext Transfer Protocol
- **HTTPS:** Hypertext Transfer Protocol Secure
- **EARS:** Easy Approach to Requirements Syntax

### **1.5 EARS Compliance**
This document follows the Easy Approach to Requirements Syntax (EARS) methodology to ensure requirements are:
- **Clear and unambiguous:** Each requirement uses simple, declarative language
- **Testable:** Requirements can be verified through analysis, testing, or review
- **Atomic:** Each requirement addresses a single concern
- **Consistent:** Requirements follow consistent patterns and terminology

**EARS Templates Used:**
- **Ubiquitous Requirements:** "The system SHALL [function]"
- **Event-Driven Requirements:** "When [trigger], the system SHALL [response]"
- **State-Driven Requirements:** "While [state], the system SHALL [behavior]"
- **Unwanted Behavior Requirements:** "The system SHALL NOT [prohibited behavior]"

---

## **2. HIGH-LEVEL REQUIREMENTS**

### **2.0 Data Structure Requirements**

#### **HLR-0.1.1: SessionState Data Structure**
**Requirement:** The SessionState data class SHALL support the following structure with specified fields and types.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides a centralized data structure for session state management across the application.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1, SR-1.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionState.kt`
**Function:** `data class SessionState`

| Type | Name | Description |
|------|------|-------------|
| Boolean | isLoggedIn | Indicates whether the user is currently logged in and authenticated |
| Boolean | isSessionExpired | Indicates whether the current session has expired due to timeout |
| Boolean | isTokenRefreshing | Indicates whether a token refresh operation is currently in progress |
| Boolean | isMaintenanceMode | Indicates whether the application is in maintenance mode |
| String? | userId | Unique identifier for the authenticated user |
| String? | userRole | Role of the authenticated user (captain, voyager, business) |
| Long | lastActivityTime | Timestamp of the last user activity for session timeout calculation |
| Long | sessionTimeoutMinutes | Configurable session timeout duration in minutes |
| Int | retryAttempts | Current number of retry attempts for failed operations |
| Int | maxRetryAttempts | Maximum allowed retry attempts before giving up |

#### **HLR-0.2.1: SessionEvent Data Structure**
**Requirement:** The SessionEvent sealed class SHALL support the following event types for session management.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides type-safe event definitions for session-related events across the application.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-6.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionEvent.kt`
**Function:** `sealed class SessionEvent`

| Type | Name | Description |
|------|------|-------------|
| object | LogoutRequired | A singleton object that represents a user-initiated logout event |
| object | SessionExpired | A singleton object that represents a session timeout expiration event |
| object | TokenRefreshFailed | A singleton object that represents a failed token refresh operation event |
| object | AccountDeactivated | A singleton object that represents an account deactivation event |
| object | MaintenanceMode | A singleton object that represents an application maintenance mode event |
| object | ForceLogout | A singleton object that represents a forced logout for security reasons event |
| object | SessionRestored | A singleton object that represents a successful session restoration event |

#### **HLR-0.3.1: TokenRefreshRequest Data Structure**
**Requirement:** The RefreshRequest data class SHALL support the following structure for token refresh operations.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for token refresh API requests.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/RefreshRequest.kt`
**Function:** `data class RefreshRequest`

| Type | Name | Description |
|------|------|-------------|
| String | accessToken | Current access token to be refreshed |
| String | refreshToken | Refresh token used to obtain new access token |

#### **HLR-0.4.1: TokenResponse Data Structure**
**Requirement:** The TokenResponse data class SHALL support the following structure for token refresh API responses.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for token refresh API responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/TokenResponse.kt`
**Function:** `data class TokenResponse`

| Type | Name | Description |
|------|------|-------------|
| Boolean | isSuccess | Indicates whether the token refresh operation was successful |
| String | message | Response message from the server |
| TokenData? | obj | Token data object containing new tokens if successful |

#### **HLR-0.4.2: TokenData Data Structure**
**Requirement:** The TokenData data class SHALL support the following structure for token information within API responses.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for token information within API responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/TokenResponse.kt`
**Function:** `data class TokenData`

| Type | Name | Description |
|------|------|-------------|
| String | accessToken | New access token received from refresh operation |
| String | refreshToken | New refresh token received from refresh operation |

#### **HLR-0.5.1: TokenData Data Structure**
**Requirement:** The TokenData data class SHALL support the following structure for token information.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for token information within API responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/TokenResponse.kt`
**Function:** `data class TokenData`

| Type | Name | Description |
|------|------|-------------|
| String | Accesstoken | New access token received from refresh operation |
| String | Refreshtoken | New refresh token received from refresh operation |

#### **HLR-0.6.1: UserData Data Structure**
**Requirement:** The UserData data class SHALL support the following structure for user information storage.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for persistent user information storage.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-4.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `data class UserData`

| Type | Name | Description |
|------|------|-------------|
| String? | userId | Unique identifier for the user |
| String? | username | Display name of the user |
| String? | userRole | Role of the user (captain, voyager, business) |
| String? | accessToken | Current access token for the user |
| String? | refreshToken | Current refresh token for the user |
| Long | loginTime | Timestamp when user logged in |
| Boolean | isLoggedIn | Whether the user is currently logged in |

---

### **2.1 Session Lifecycle Management**

#### **HLR-1.1.1: Session Initialization Trigger**
**Requirement:** When the user logs in successfully, the function `handleLogin(userId: String?, userRole: String?)` SHALL initialize a new session.
**EARS Template:** Event-Driven Requirement
**Rationale:** Establishes secure session foundation upon user authentication.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleLogin(userId: String?, userRole: String?)`

#### **HLR-1.1.2: Session Data Population**
**Requirement:** When initializing a session, the function `handleLogin(userId: String?, userRole: String?)` SHALL populate session data with user ID, username, user role, and authentication tokens.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures session contains all necessary user context for application functionality.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleLogin(userId: String?, userRole: String?)` - populates `_sessionState.value`

#### **HLR-1.1.3: Session State Initialization**
**Requirement:** When initializing a session, the function `handleLogin(userId: String?, userRole: String?)` SHALL set session state to active and record initialization timestamp.
**EARS Template:** Event-Driven Requirement
**Rationale:** Establishes baseline session state for monitoring and timeout management.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleLogin(userId: String?, userRole: String?)` - sets `isLoggedIn = true` and `lastActivityTime`

#### **HLR-1.2.1: Session State Persistence During Activity**
**Requirement:** While the user is active, the function `updateLastActivity()` SHALL persist session state to secure storage.
**EARS Template:** State-Driven Requirement
**Rationale:** Ensures session data survives application state changes during active use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `updateLastActivity()`

#### **HLR-1.2.2: Activity Timestamp Updates**
**Requirement:** While the user is active, the function `updateLastActivity()` SHALL update the last activity timestamp on significant user interactions.
**EARS Template:** State-Driven Requirement
**Rationale:** Enables accurate session timeout monitoring based on user activity.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `updateLastActivity()`

#### **HLR-1.2.3: Session State Synchronization**
**Requirement:** While the user is active, the function `GlobalSessionHandler()` SHALL synchronize session state across all application components.
**EARS Template:** State-Driven Requirement
**Rationale:** Ensures consistent session state throughout the application.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

#### **HLR-1.3.1: Periodic Session Validation**
**Requirement:** The function `isSessionValid(): Boolean` SHALL validate session integrity every 30 seconds.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures session integrity through periodic validation.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `isSessionValid(): Boolean`

#### **HLR-1.3.2: Token Expiry Validation**
**Requirement:** The function `refreshToken(): Boolean` SHALL validate token expiry during periodic session validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are still valid and triggers refresh if needed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-1.3.3: Session Timeout Validation**
**Requirement:** The function `isSessionValid(): Boolean` SHALL validate session timeout during periodic session validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures session hasn't exceeded timeout threshold.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `isSessionValid(): Boolean`

#### **HLR-1.4.1: User-Initiated Logout**
**Requirement:** When the user logs out, the function `handleLogout()` SHALL terminate the session.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures proper session cleanup when user explicitly logs out.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleLogout()`

#### **HLR-1.4.2: Timeout-Initiated Logout**
**Requirement:** When the session timeout expires, the function `handleSessionExpired()` SHALL terminate the session.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures security by terminating abandoned sessions.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleSessionExpired()`

#### **HLR-1.4.3: Session Data Cleanup on Termination**
**Requirement:** When terminating a session, the function `performLogout()` SHALL clean up all session data from memory and storage.
**EARS Template:** Event-Driven Requirement
**Rationale:** Prevents data leakage and ensures proper resource management.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `performLogout()` (private)

---

### **2.2 Authentication Token Management**

#### **HLR-2.1.1: Access Token Storage**
**Requirement:** The function `saveTokens(accessToken: String?, refreshToken: String?)` SHALL store access tokens securely using Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Prevents unauthorized access to stored authentication tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `saveTokens(accessToken: String?, refreshToken: String?)`

#### **HLR-2.1.2: Refresh Token Storage**
**Requirement:** The function `saveTokens(accessToken: String?, refreshToken: String?)` SHALL store refresh tokens securely using Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Prevents unauthorized access to stored refresh tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `saveTokens(accessToken: String?, refreshToken: String?)`

#### **HLR-2.1.3: Token Storage Encryption**
**Requirement:** The function `saveLoginData(userData: UserData)` SHALL encrypt tokens before storing them in Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides additional security layer for stored tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `saveLoginData(userData: UserData)`

#### **HLR-2.2.1: Token Expiry Detection**
**Requirement:** When the access token expires, the function `refreshToken(): Boolean` SHALL detect the expiry condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables automatic token refresh when needed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-2.2.2: Refresh Token Usage**
**Requirement:** When refreshing an expired access token, the function `refreshToken(): Boolean` SHALL use the stored refresh token.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures proper authentication flow using refresh token.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-2.2.3: New Token Storage**
**Requirement:** When token refresh succeeds, the function `refreshToken(): Boolean` SHALL store the new access token securely.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures new tokens are properly stored for future use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean` - calls `tokenProvider.saveTokens()`

#### **HLR-2.3.1: Token Format Validation**
**Requirement:** The function `getAccessToken(): String?` SHALL validate token format before each network request.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are properly formatted before use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `getAccessToken(): String?`

#### **HLR-2.3.2: Token Expiry Validation**
**Requirement:** The function `refreshToken(): Boolean` SHALL validate token expiry before each network request.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens haven't expired before use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-2.3.3: Token Signature Validation**
**Requirement:** The function `refreshToken(): Boolean` SHALL validate token signature before each network request.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are authentic and haven't been tampered with.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-2.4.1: Refresh Failure Detection**
**Requirement:** When token refresh fails, the function `refreshToken(): Boolean` SHALL detect the failure condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of refresh failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `refreshToken(): Boolean`

#### **HLR-2.4.2: Logout Trigger on Refresh Failure**
**Requirement:** When token refresh fails, the function `handleTokenRefreshFailed()` SHALL trigger user logout.
**EARS Template:** Event-Driven Requirement
**Rationale:** Maintains security by logging out users when authentication fails.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleTokenRefreshFailed()`

#### **HLR-2.4.3: Session Cleanup on Refresh Failure**
**Requirement:** When token refresh fails, the function `performLogout()` SHALL clean up session data.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures no stale session data remains after authentication failure.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `performLogout()` (private)

---

### **2.3 Network Error Handling and Recovery**

#### **HLR-3.1.1: Server Error Detection**
**Requirement:** When a server error (HTTP 5xx) occurs, the function `intercept()` SHALL detect the error condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of server errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`


#### **HLR-3.1.3: Server Error Retry Logic**
**Requirement:** When a server error (HTTP 5xx) occurs, the function `intercept()` SHALL retry the request.
**EARS Template:** Event-Driven Requirement
**Rationale:** Attempts to recover from temporary server issues.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **HLR-3.2.1: Timeout Error Detection**
**Requirement:** When a timeout error occurs, the function `intercept()` SHALL detect the timeout condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of timeout errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`


#### **HLR-3.2.3: Timeout Error Retry Logic**
**Requirement:** When a timeout error occurs, the function `intercept()` SHALL retry the request.
**EARS Template:** Event-Driven Requirement
**Rationale:** Attempts to recover from temporary network timeout issues.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **HLR-3.3.1: Client Error Detection**
**Requirement:** When a client error (HTTP 4xx) occurs, the function `intercept()` SHALL detect the error condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of client errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **HLR-3.3.2: Client Error No-Retry Policy**
**Requirement:** When a client error (HTTP 4xx) occurs, the function `intercept()` SHALL NOT retry the request.
**EARS Template:** Unwanted Behavior Requirement
**Rationale:** Prevents unnecessary retries for permanent client-side errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`



---

### **2.4 Session State Persistence**

#### **HLR-4.1.1: Session State Storage**
**Requirement:** The function `saveLoginData(userData: UserData)` SHALL store session state to secure storage.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures session data survives application restarts and device state changes.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-4.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `saveLoginData(userData: UserData)`

#### **HLR-4.1.2: Session State Retrieval**
**Requirement:** The function `getUserData(): UserData?` SHALL retrieve session state from secure storage on application startup.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables session restoration after application restart.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-4.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `getUserData(): UserData?`

#### **HLR-4.1.3: Session State Encryption**
**Requirement:** The function `saveLoginData(userData: UserData)` SHALL encrypt session state before storing it.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Protects sensitive session data from unauthorized access.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-4.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `saveLoginData(userData: UserData)`

---

### **2.5 Security Event Handling**

#### **HLR-5.1.1: Unauthorized Response Detection**
**Requirement:** When an HTTP 401 response is received, the function `handleUnauthorized(): Boolean` SHALL detect the unauthorized condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of unauthorized responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleUnauthorized(): Boolean`

#### **HLR-5.1.2: Token Refresh Attempt**
**Requirement:** When an HTTP 401 response is received, the function `handleUnauthorized(): Boolean` SHALL attempt token refresh.
**EARS Template:** Event-Driven Requirement
**Rationale:** Handles expired or invalid tokens by attempting refresh.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleUnauthorized(): Boolean`

#### **HLR-5.1.3: Request Retry After Refresh**
**Requirement:** When token refresh succeeds after HTTP 401, the function `intercept()` SHALL retry the original request.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures the original request is completed after successful token refresh.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **HLR-5.2.1: Forbidden Response Detection**
**Requirement:** When an HTTP 403 response is received, the function `intercept()` SHALL detect the forbidden condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of forbidden responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

#### **HLR-5.2.2: Immediate Logout Trigger**
**Requirement:** When an HTTP 403 response is received, the function `handleAccountDeactivated()` SHALL trigger immediate logout.
**EARS Template:** Event-Driven Requirement
**Rationale:** Protects against unauthorized access by logging out users.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleAccountDeactivated()`

#### **HLR-5.2.3: Security Event Logging**
**Requirement:** When an HTTP 403 response is received, the function `intercept()` SHALL log the security event.
**EARS Template:** Event-Driven Requirement
**Rationale:** Provides audit trail for security violations.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt`
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse`

---

### **2.6 Global Session Event Broadcasting**

#### **HLR-6.1.1: Kotlin Flow Implementation**
**Requirement:** The function `GlobalSessionHandler()` SHALL implement session event broadcasting using Kotlin Flow.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures reliable event delivery using reactive programming.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-6.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

#### **HLR-6.1.2: Event Type Definition**
**Requirement:** The function `SessionEvent` SHALL define specific event types for session events.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables structured event handling and type safety.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-6.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionEvent.kt`
**Function:** `sealed class SessionEvent`

#### **HLR-6.1.3: Event Publisher Implementation**
**Requirement:** The function `_sessionEvents.emit(event)` SHALL implement an event publisher for session events.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides centralized mechanism for broadcasting session events.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-6.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `_sessionEvents.emit(event)` (via various handler functions)

#### **HLR-6.2.1: Event Subscription Mechanism**
**Requirement:** The function `GlobalSessionHandler()` SHALL provide a mechanism for components to subscribe to session events.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables components to receive session events they need.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-6.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

---

### **2.7 Session Timeout Management**

#### **HLR-7.1.1: Default Timeout Configuration**
**Requirement:** The function `SessionManager` SHALL configure session timeout to 30 minutes by default.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides reasonable session duration for security and usability.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` constructor - `SESSION_TIMEOUT_MINUTES`


#### **HLR-7.2.1: Timeout Warning Trigger**
**Requirement:** When session timeout approaches, the function `startSessionMonitoring()` SHALL trigger timeout warnings.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proactive user notification before session expiration.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `startSessionMonitoring()`


#### **HLR-7.3.1: Timeout Expiration Detection**
**Requirement:** When session timeout expires, the function `startSessionMonitoring()` SHALL detect the expiration condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of session timeout expiration.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `startSessionMonitoring()`

#### **HLR-7.3.2: Automatic Logout Trigger**
**Requirement:** When session timeout expires, the function `handleSessionExpired()` SHALL trigger automatic logout.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures security by terminating abandoned sessions.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `handleSessionExpired()`

#### **HLR-7.3.3: Timeout Event Broadcasting**
**Requirement:** When session timeout expires, the function `_sessionEvents.emit(SessionEvent.SessionExpired)` SHALL broadcast timeout events.
**EARS Template:** Event-Driven Requirement
**Rationale:** Notifies all components of session timeout for proper cleanup.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `_sessionEvents.emit(SessionEvent.SessionExpired)` (via `handleSessionExpired()`)

#### **HLR-7.4.1: Session Data Cleanup on Timeout**
**Requirement:** When session timeout occurs, the function `performLogout()` SHALL clean up session data.
**EARS Template:** Event-Driven Requirement
**Rationale:** Prevents data leakage and ensures proper resource management.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-7.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `performLogout()` (private)


---

---

### **2.9 Performance Requirements**

#### **HLR-9.1.1: Session Operation Performance Target**
**Requirement:** The function `SessionManager` SHALL complete session operations within 100ms for 95% of requests.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures responsive user experience for session-related operations.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-9.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` (all operations)

---

### **2.10 Configuration Management**

#### **HLR-10.1.1: External Configuration Support**
**Requirement:** The function `SessionManager` SHALL support external configuration of session parameters.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables system customization without code changes.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-10.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `SessionManager` constructor

---

### **2.11 Integration and Compatibility**

#### **HLR-11.1.1: Integration Interface Definition**
**Requirement:** The function `GlobalSessionHandler()` SHALL define clear integration interfaces for existing application components.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables seamless integration with existing application components.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-11.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/session/GlobalSessionHandler.kt`
**Function:** `GlobalSessionHandler(navController: NavController, sessionManager: SessionManager)`

---

### **2.12 Data Privacy and Protection**

#### **HLR-12.1.1: Data Encryption at Rest**
**Requirement:** The function `saveLoginData(userData: UserData)` SHALL encrypt user data at rest.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Protects sensitive user data from unauthorized access when stored.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-12.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/Prefmanager.kt`
**Function:** `saveLoginData(userData: UserData)`

---

## **3. REQUIREMENT DEPENDENCIES**

| HLR ID | Depends On | Dependency Type |
|--------|------------|-----------------|
| HLR-0.1.1 | None | Foundation |
| HLR-0.1.2 | None | Foundation |
| HLR-0.1.3 | None | Foundation |
| HLR-0.1.4 | None | Foundation |
| HLR-0.1.5 | HLR-0.1.4 | Functional |
| HLR-0.1.6 | None | Foundation |
| HLR-0.4.2 | HLR-0.4.1 | Functional |
| HLR-1.1.1 | HLR-0.1.1, HLR-0.1.6 | Functional |
| HLR-1.1.2 | HLR-1.1.1 | Functional |
| HLR-1.1.3 | HLR-1.1.1 | Functional |
| HLR-1.2.1 | HLR-1.1.1 | Functional |
| HLR-1.2.2 | HLR-1.1.1 | Functional |
| HLR-1.2.3 | HLR-1.1.1 | Functional |
| HLR-1.3.1 | HLR-1.1.1 | Functional |
| HLR-1.3.2 | HLR-1.1.1 | Functional |
| HLR-1.3.3 | HLR-1.1.1 | Functional |
| HLR-1.4.1 | HLR-1.1.1 | Functional |
| HLR-1.4.2 | HLR-1.3.1 | Functional |
| HLR-1.4.3 | HLR-1.4.1, HLR-1.4.2 | Functional |
| HLR-2.1.2 | HLR-2.1.1 | Functional |
| HLR-2.1.3 | HLR-2.1.1 | Functional |
| HLR-2.2.1 | HLR-2.1.1 | Functional |
| HLR-2.2.2 | HLR-2.1.2 | Functional |
| HLR-2.2.3 | HLR-2.2.2 | Functional |
| HLR-2.3.1 | HLR-2.1.1 | Functional |
| HLR-2.3.2 | HLR-2.1.1 | Functional |
| HLR-2.3.3 | HLR-2.1.1 | Functional |
| HLR-2.4.1 | HLR-2.2.1 | Functional |
| HLR-2.4.2 | HLR-2.4.1 | Functional |
| HLR-2.4.3 | HLR-2.4.2 | Functional |
| HLR-3.1.1 | HLR-2.3.1 | Functional |
| HLR-3.1.3 | HLR-3.1.1 | Functional |
| HLR-3.2.1 | HLR-2.3.1 | Functional |
| HLR-3.2.3 | HLR-3.2.1 | Functional |
| HLR-3.3.1 | HLR-2.3.1 | Functional |
| HLR-3.3.2 | HLR-3.3.1 | Functional |
| HLR-4.1.2 | HLR-4.1.1 | Functional |
| HLR-4.1.3 | HLR-4.1.1 | Functional |
| HLR-5.1.1 | HLR-2.2.1 | Functional |
| HLR-5.1.2 | HLR-5.1.1 | Functional |
| HLR-5.1.3 | HLR-5.1.2 | Functional |
| HLR-5.2.1 | HLR-2.4.1 | Functional |
| HLR-5.2.2 | HLR-5.2.1 | Functional |
| HLR-5.2.3 | HLR-5.2.1 | Functional |
| HLR-6.1.2 | HLR-6.1.1 | Functional |
| HLR-6.1.3 | HLR-6.1.1 | Functional |
| HLR-6.2.1 | HLR-6.1.1 | Functional |
| HLR-7.1.1 | HLR-6.1.1 | Functional |
| HLR-7.2.1 | HLR-7.1.1 | Functional |
| HLR-7.3.1 | HLR-7.1.1 | Functional |
| HLR-7.3.2 | HLR-7.3.1 | Functional |
| HLR-7.3.3 | HLR-7.3.1 | Functional |
| HLR-7.4.1 | HLR-7.3.2 | Functional |
| HLR-9.1.1 | HLR-7.1.1 | Functional |
| HLR-10.1.1 | HLR-9.1.1 | Functional |
| HLR-11.1.1 | HLR-10.1.1 | Functional |
| HLR-12.1.1 | HLR-11.1.1 | Functional |

---

## **4. VERIFICATION AND VALIDATION**

### **4.1 Verification Methods**
- **Analysis:** Requirements analysis, design analysis, code analysis
- **Testing:** Unit testing, integration testing, system testing
- **Review:** Peer review, inspection, walkthrough

### **4.2 Validation Criteria**
Each high-level requirement shall be considered satisfied when:
1. All associated system requirements (SRs) are implemented and verified
2. Verification evidence demonstrates compliance
3. Integration testing confirms proper system behavior
4. Performance testing validates constraint compliance
5. Security analysis confirms appropriate protection measures
6. Documentation is complete and accurate

---

## **5. CHANGE CONTROL**

Any changes to high-level requirements shall follow the established change control process:
1. Change request submission
2. Impact analysis
3. Review and approval
4. Implementation
5. Verification
6. Documentation update

---

## **6. APPENDICES**

### **Appendix A: Glossary**
- **Session:** A period of interaction between a user and the system
- **Token:** A credential used for authentication and authorization
- **Retry Logic:** Automatic retry mechanism for failed operations
- **Exponential Backoff:** Increasing delay between retry attempts

### **Appendix B: References**
- DO-178C Software Considerations in Airborne Systems
- Android Security Best Practices
- OWASP Mobile Security Guidelines
- Kotlin Programming Language Documentation

---

**Document Control:**
- **Status:** Draft
- **Distribution:** Development Team, QA Team, Management
- **Next Review Date:** [Date + 30 days]
- **Approval Required:** Technical Lead, QA Lead, Project Manager
