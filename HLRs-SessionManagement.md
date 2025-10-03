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
| String? | accessToken | Current access token to be refreshed |
| String? | refreshToken | Refresh token used to obtain new access token |

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
| Int | status | HTTP status code of the token refresh response |
| String | message | Response message from the server |
| TokenData | obj | Token data object containing new tokens |

#### **HLR-0.4.2: TokenData Data Structure**
**Requirement:** The TokenData data class SHALL support the following structure for token information within API responses.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for token information within API responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/RefreshResponse.kt`
**Function:** `data class TokenData`

| Type | Name | Description |
|------|------|-------------|
| String | accessToken | New access token received from refresh operation |
| String | refreshToken | New refresh token received from refresh operation |

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
| String? | email | Email address of the user |
| String? | password | User password (not stored in HLR, for API use only) |
| String? | userId | Unique identifier for the user |
| String? | username | Display name of the user |
| String? | userRole | Role of the user (captain, voyager, business) |
| Int | missingStep | Step number in user onboarding process |
| String? | accessToken | Current access token for the user |
| String? | refreshToken | Current refresh token for the user |
| Long | loginTime | Timestamp when user logged in |
| Boolean | isLoggedIn | Whether the user is currently logged in |

#### **HLR-0.7.1: LoginResponse Data Structure**
**Requirement:** The LoginResponse data class SHALL support the following structure for login API responses.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for login API responses containing user authentication data.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1, SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/login/model/loginResponse.kt`
**Function:** `data class LoginResponse`

| Type | Name | Description |
|------|------|-------------|
| Int | status | HTTP status code of the login response |
| String | message | Response message from the server |
| UserData? | obj | User data object containing user information if login successful |

#### **HLR-0.8.1: NetworkResponse Data Structure**
**Requirement:** The NetworkResponse sealed class SHALL support the following generic response structure for network operations.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides type-safe generic response handling for all network operations with success, error, and loading states.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1, SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/networkreposne/NetworkResponse.kt`
**Function:** `sealed class NetworkResponse<T>`

| Type | Name | Description |
|------|------|-------------|
| Success<T> | Success | Represents successful network operation with data |
| Error<T> | Error | Represents failed network operation with error message |
| Loading<T> | Loading | Represents ongoing network operation |

#### **HLR-0.9.1: ApiError Data Structure**
**Requirement:** The ApiError data class SHALL support the following structure for API error handling and reporting.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured error information for consistent error handling across the application.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1, SR-5.1.1, SR-8.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/ApiError.kt`
**Function:** `data class ApiError`

| Type | Name | Description |
|------|------|-------------|
| Int | code | HTTP status code or application-specific error code |
| String | message | Human-readable error message |
| String? | details | Additional error details or stack trace |
| Long | timestamp | Timestamp when the error occurred |
| String? | requestId | Unique identifier for the request that caused the error |

#### **HLR-0.10.1: CaptainStatus Data Structure**
**Requirement:** The CaptainStatus data class SHALL support the following structure for captain availability status management.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides structured data format for managing captain online/offline status and availability.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-4.1.1, SR-6.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/StatusProvider.kt`
**Function:** `data class CaptainStatus`

| Type | Name | Description |
|------|------|-------------|
| Boolean | isOnline | Indicates whether the captain is currently online and available |
| Long | lastSeenTime | Timestamp of when the captain was last seen online |
| String? | captainId | Unique identifier for the captain |
| String? | statusMessage | Optional status message from the captain |

---

### **2.1 Session Lifecycle Management**

#### **HLR-1.1.1: Session Initialization Trigger**
**Requirement:** When the user logs in successfully, the function `initializeSession()` SHALL initialize a new session.
**EARS Template:** Event-Driven Requirement
**Rationale:** Establishes secure session foundation upon user authentication.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `initializeSession()`

#### **HLR-1.1.2: Session Data Population**
**Requirement:** When initializing a session, the function `initializeSession()` SHALL populate session data with user ID, username, user role, and authentication tokens.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures session contains all necessary user context for application functionality.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `initializeSession()`

#### **HLR-1.1.3: Session State Initialization**
**Requirement:** When initializing a session, the function `initializeSession()` SHALL set session state to active and record initialization timestamp.
**EARS Template:** Event-Driven Requirement
**Rationale:** Establishes baseline session state for monitoring and timeout management.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/SessionManager.kt`
**Function:** `initializeSession()`

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
**Requirement:** The function `validateTokenFormat(token: String): Boolean` SHALL validate token expiry during periodic session validation.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are still valid and triggers refresh if needed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `validateTokenFormat(token: String): Boolean`

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
**Requirement:** When the access token expires, the function `makeRefreshRequest(): TokenResponse?` SHALL detect the expiry condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables automatic token refresh when needed.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `makeRefreshRequest(): TokenResponse?`

#### **HLR-2.2.2: Refresh Token Usage**
**Requirement:** When refreshing an expired access token, the function `makeRefreshRequest(): TokenResponse?` SHALL use the stored refresh token.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures proper authentication flow using refresh token.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `makeRefreshRequest(): TokenResponse?`

#### **HLR-2.2.3: New Token Storage**
**Requirement:** When token refresh succeeds, the function `processRefreshResponse(response: TokenResponse): Boolean` SHALL store the new access token securely.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures new tokens are properly stored for future use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `processRefreshResponse(response: TokenResponse): Boolean` - calls `tokenProvider.saveTokens()`

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
**Requirement:** The function `validateTokenFormat(token: String): Boolean` SHALL validate token expiry before each network request.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens haven't expired before use.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `validateTokenFormat(token: String): Boolean`

#### **HLR-2.3.3: Token Signature Validation**
**Requirement:** The function `validateTokenFormat(token: String): Boolean` SHALL validate token signature before each network request.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are authentic and haven't been tampered with.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `validateTokenFormat(token: String): Boolean`

#### **HLR-2.4.1: Refresh Failure Detection**
**Requirement:** When token refresh fails, the function `processRefreshResponse(response: TokenResponse): Boolean` SHALL detect the failure condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of refresh failures.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `processRefreshResponse(response: TokenResponse): Boolean`

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

#### **HLR-2.5.1: Malformed Token Response Detection**
**Requirement:** When token refresh returns malformed data, the function `detectMalformedResponse(response: TokenResponse): Boolean` SHALL detect malformed response conditions.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of invalid token data from server.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `detectMalformedResponse(response: TokenResponse): Boolean`

#### **HLR-2.5.2: Malformed Token Response Handling**
**Requirement:** When malformed token response is detected, the function `handleMalformedResponse(response: TokenResponse): Boolean` SHALL handle gracefully and trigger logout if necessary.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures system stability when server returns invalid token data.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `handleMalformedResponse(response: TokenResponse): Boolean`

#### **HLR-2.6.1: Token Format Validation**
**Requirement:** The function `validateTokenFormat(token: String): Boolean` SHALL validate token formats before using them.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Prevents system errors from malformed or corrupted tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.6
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `validateTokenFormat(token: String): Boolean`

#### **HLR-2.6.2: Invalid Token Format Handling**
**Requirement:** When invalid token format is detected, the function `validateTokenFormat(token: String): Boolean` SHALL reject invalid tokens and trigger appropriate actions.
**EARS Template:** Event-Driven Requirement
**Rationale:** Prevents system errors from malformed or corrupted tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.6
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/session/TokenRefreshService.kt`
**Function:** `validateTokenFormat(token: String): Boolean`

#### **HLR-2.7.1: Keystore Key Generation**
**Requirement:** The function `generateEncryptionKey(): SecretKey` SHALL generate AES encryption keys for token storage.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides secure encryption keys for protecting tokens in Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `generateEncryptionKey(): SecretKey`

#### **HLR-2.7.2: Token Encryption**
**Requirement:** The function `encryptToken(token: String, key: SecretKey): String` SHALL encrypt tokens before Keystore storage.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are encrypted before being stored in Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `encryptToken(token: String, key: SecretKey): String`

#### **HLR-2.7.3: Keystore Token Storage**
**Requirement:** The function `storeEncryptedToken(keyAlias: String, encryptedToken: String)` SHALL store encrypted tokens in Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides secure storage of encrypted tokens in Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `storeEncryptedToken(keyAlias: String, encryptedToken: String)`

#### **HLR-2.7.4: Token Decryption**
**Requirement:** The function `decryptToken(encryptedToken: String, key: SecretKey): String` SHALL decrypt tokens retrieved from Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Enables secure retrieval and decryption of tokens from Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `decryptToken(encryptedToken: String, key: SecretKey): String`

#### **HLR-2.7.5: Keystore Token Retrieval**
**Requirement:** The function `retrieveEncryptedToken(keyAlias: String): String?` SHALL retrieve encrypted tokens from Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides secure retrieval of encrypted tokens from Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `retrieveEncryptedToken(keyAlias: String): String?`

#### **HLR-2.8.1: Login Response Token Extraction**
**Requirement:** When login succeeds, the function `saveLoginData(userData: UserData)` SHALL extract access and refresh tokens from the login response.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures tokens from login response are properly extracted for secure storage.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.1, SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/ui/login/viewmodel/LoginViewModel.kt`
**Function:** `saveLoginData(userData: UserData)`

#### **HLR-2.8.2: Token Processing for Keystore**
**Requirement:** The function `processTokensForKeystore(accessToken: String, refreshToken: String)` SHALL prepare tokens for secure Keystore storage.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures tokens are properly processed before being stored in Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `processTokensForKeystore(accessToken: String, refreshToken: String)`

#### **HLR-2.8.3: Keystore Token Storage Implementation**
**Requirement:** The function `storeTokensInKeystore(accessToken: String, refreshToken: String)` SHALL store both tokens securely in Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides complete implementation for storing both access and refresh tokens in Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `storeTokensInKeystore(accessToken: String, refreshToken: String)`

#### **HLR-2.8.4: Keystore Token Retrieval Implementation**
**Requirement:** The function `getAccessToken(): String?` SHALL retrieve and decrypt access tokens from Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Provides complete implementation for retrieving and decrypting access tokens from Android Keystore.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-2.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `getAccessToken(): String?`

#### **HLR-2.8.5: Keystore Token Cleanup**
**Requirement:** The function `clearTokens()` SHALL remove all tokens from Android Keystore.
**EARS Template:** Ubiquitous Requirement
**Rationale:** Ensures complete cleanup of tokens from Android Keystore during logout or session termination.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-1.1.4
**Source File:** `app/src/main/java/com/boatit/boatsharing/utils/prefmanager/TokenProvider.kt`
**Function:** `clearTokens()`

#### **HLR-3.5.1: Malformed Network Response Detection**
**Requirement:** When API responses contain malformed data, the function `detectMalformedResponse(response: Response): Boolean` SHALL detect malformed response conditions.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of invalid response data from APIs.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `detectMalformedResponse(response: Response): Boolean` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Malformed response detection is handled by Ktor's Logging plugin and error handling mechanisms.

#### **HLR-3.5.2: Malformed Network Response Handling**
**Requirement:** When malformed network response is detected, the function `handleMalformedResponse(response: Response): Response` SHALL handle gracefully without crashing.
**EARS Template:** Event-Driven Requirement
**Rationale:** Ensures system stability when receiving invalid response data.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `handleMalformedResponse(response: Response): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Response handling is now managed by Ktor's built-in error handling and logging mechanisms.

---

### **2.3 Network Error Handling and Recovery**

#### **HLR-3.1.1: Server Error Detection**
**Requirement:** When a server error (HTTP 5xx) occurs, the function `handleServerError(response: Response): Response` SHALL detect the error condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of server errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `handleServerError(response: Response): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Server error handling is now managed by Ktor's HttpRequestRetry plugin with exponential backoff.


#### **HLR-3.1.3: Server Error Retry Logic**
**Requirement:** When a server error (HTTP 5xx) occurs, the function `applyRetryLogic(request: Request, attempt: Int): Response` SHALL retry the request.
**EARS Template:** Event-Driven Requirement
**Rationale:** Attempts to recover from temporary server issues.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `applyRetryLogic(request: Request, attempt: Int): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Retry logic is now handled by Ktor's HttpRequestRetry plugin with configurable exponential backoff.

#### **HLR-3.2.1: Timeout Error Detection**
**Requirement:** When a timeout error occurs, the function `handleTimeoutError(exception: Exception): Response` SHALL detect the timeout condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of timeout errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `handleTimeoutError(exception: Exception): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Timeout handling is now managed by Ktor's HttpTimeout plugin and HttpRequestRetry plugin.


#### **HLR-3.2.3: Timeout Error Retry Logic**
**Requirement:** When a timeout error occurs, the function `applyRetryLogic(request: Request, attempt: Int): Response` SHALL retry the request.
**EARS Template:** Event-Driven Requirement
**Rationale:** Attempts to recover from temporary network timeout issues.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `applyRetryLogic(request: Request, attempt: Int): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Retry logic is now handled by Ktor's HttpRequestRetry plugin with configurable exponential backoff.

#### **HLR-3.3.1: Client Error Detection**
**Requirement:** When a client error (HTTP 4xx) occurs, the function `handleClientError(response: Response): Response` SHALL detect the error condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of client errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `handleClientError(response: Response): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Client error handling is now managed by Ktor's Auth plugin for authentication errors and built-in error handling for other client errors.

#### **HLR-3.3.2: Client Error No-Retry Policy**
**Requirement:** When a client error (HTTP 4xx) occurs, the function `handleClientError(response: Response): Response` SHALL NOT retry the request.
**EARS Template:** Unwanted Behavior Requirement
**Rationale:** Prevents unnecessary retries for permanent client-side errors.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `handleClientError(response: Response): Response` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Client error handling is now managed by Ktor's Auth plugin for authentication errors and built-in error handling for other client errors.



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
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Network interception is now handled by Ktor's native plugins: HttpRequestRetry, Auth, HttpTimeout, and Logging.

#### **HLR-5.2.1: Forbidden Response Detection**
**Requirement:** When an HTTP 403 response is received, the function `intercept()` SHALL detect the forbidden condition.
**EARS Template:** Event-Driven Requirement
**Rationale:** Enables proper handling of forbidden responses.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Network interception is now handled by Ktor's native plugins: HttpRequestRetry, Auth, HttpTimeout, and Logging.

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
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/interceptors/NetworkInterceptor.kt` *(DEPRECATED)*
**Function:** `intercept(request: HttpRequestBuilder, execute: suspend (HttpRequestBuilder) -> HttpResponse): HttpResponse` *(DEPRECATED)*
**Migration Note:** This functionality has been migrated to native Ktor implementation in `KtorClient.kt`. Network interception is now handled by Ktor's native plugins: HttpRequestRetry, Auth, HttpTimeout, and Logging.

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

## **2.4 NetworkInterceptor Migration to Native Ktor**

**Migration Status:** ✅ **COMPLETE**

The `NetworkInterceptor.kt` class has been **deprecated** and its functionality has been successfully migrated to `KtorClient.kt` using native Ktor plugins.

### **New Implementation Details**

**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor(tokenProvider: TokenProvider, sessionManager: SessionManager): HttpClient`

### **Migration Benefits**

1. **Better Performance**: Uses Ktor's optimized native plugins
2. **Automatic Token Refresh**: Seamless token refresh via Auth plugin
3. **Native Integration**: Built-in timeout handling and retry logic
4. **Simplified Maintenance**: Less custom code to maintain
5. **Future-Proof**: Uses Ktor's maintained APIs

### **Feature Migration Status**

| Feature | Status | Implementation |
|---------|--------|----------------|
| Server Error Retry (5xx) | ✅ Migrated | Ktor HttpRequestRetry plugin |
| Timeout Error Retry | ✅ Migrated | Ktor HttpRequestRetry plugin |
| Client Error Handling (4xx) | ✅ Migrated | Ktor Auth plugin |
| Exception-based Retry | ✅ Migrated | Ktor HttpRequestRetry plugin |
| Session Management | ✅ Migrated | Ktor Auth plugin + SessionManager |
| Token Refresh | ✅ Migrated | Automatic via Auth plugin |
| Logging | ✅ Migrated | Ktor Logging plugin |
| Timeout Configuration | ✅ Improved | Ktor HttpTimeout plugin |
| Malformed Response Detection | ⚠️ Non-critical | Not implemented |

---

## **2.5 Native Ktor Network Implementation Requirements**

### **HLR-3.10.1: Native Ktor HttpClient Configuration**
**Requirement:** The system SHALL use Ktor's native HttpClient with configured plugins for network operations.
**EARS Template:** System Requirement
**Rationale:** Provides optimized, maintainable network layer using Ktor's native capabilities.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1, SR-3.1.2, SR-3.1.3
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor(tokenProvider: TokenProvider, sessionManager: SessionManager): HttpClient`

### **HLR-3.10.2: HttpRequestRetry Plugin Configuration**
**Requirement:** The system SHALL configure Ktor's HttpRequestRetry plugin with exponential backoff for automatic retry logic.
**EARS Template:** System Requirement
**Rationale:** Enables automatic recovery from temporary network issues using native Ktor retry mechanisms.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.1, SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - HttpRequestRetry plugin configuration

### **HLR-3.10.3: Auth Plugin Bearer Token Management**
**Requirement:** The system SHALL use Ktor's Auth plugin for automatic bearer token management and refresh.
**EARS Template:** System Requirement
**Rationale:** Provides seamless authentication handling with automatic token refresh capabilities.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1, SR-5.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - Auth plugin bearer configuration

### **HLR-3.10.4: HttpTimeout Plugin Configuration**
**Requirement:** The system SHALL configure Ktor's HttpTimeout plugin for comprehensive timeout management.
**EARS Template:** System Requirement
**Rationale:** Ensures proper timeout handling for requests, connections, and socket operations.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.2
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - HttpTimeout plugin configuration

### **HLR-3.10.5: Logging Plugin Configuration**
**Requirement:** The system SHALL use Ktor's Logging plugin for comprehensive request and response logging.
**EARS Template:** System Requirement
**Rationale:** Provides detailed logging for debugging and monitoring network operations.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - Logging plugin configuration

### **HLR-3.10.6: ContentNegotiation Plugin Configuration**
**Requirement:** The system SHALL use Ktor's ContentNegotiation plugin for JSON serialization and deserialization.
**EARS Template:** System Requirement
**Rationale:** Enables proper JSON handling with ignoreUnknownKeys configuration for API compatibility.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-3.1.5
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - ContentNegotiation plugin configuration

### **HLR-3.10.7: Automatic Token Refresh Implementation**
**Requirement:** The system SHALL implement automatic token refresh through Ktor's Auth plugin refreshTokens block.
**EARS Template:** System Requirement
**Rationale:** Ensures seamless user experience by automatically refreshing expired tokens.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - Auth plugin refreshTokens block

### **HLR-3.10.8: Default Request Headers Configuration**
**Requirement:** The system SHALL configure default request headers including Authorization header with bearer tokens.
**EARS Template:** System Requirement
**Rationale:** Ensures consistent authentication headers across all network requests.
**Safety Classification:** DAL D
**Verification Method:** Analysis, Testing
**Traces to:** SR-5.1.1
**Source File:** `app/src/main/java/com/boatit/boatsharing/network/di/KtorClient.kt`
**Function:** `createKtorClientWithInterceptor()` - defaultRequest configuration

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
| HLR-0.6.1 | None | Foundation |
| HLR-0.7.1 | HLR-0.6.1 | Functional |
| HLR-0.8.1 | None | Foundation |
| HLR-0.9.1 | HLR-0.8.1 | Functional |
| HLR-0.10.1 | None | Foundation |
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
| HLR-2.5.1 | HLR-2.2.1 | Functional |
| HLR-2.5.2 | HLR-2.5.1 | Functional |
| HLR-2.6.1 | HLR-2.3.1 | Functional |
| HLR-2.6.2 | HLR-2.6.1 | Functional |
| HLR-2.7.1 | HLR-2.1.1 | Functional |
| HLR-2.7.2 | HLR-2.7.1 | Functional |
| HLR-2.7.3 | HLR-2.7.2 | Functional |
| HLR-2.7.4 | HLR-2.7.1 | Functional |
| HLR-2.7.5 | HLR-2.7.3 | Functional |
| HLR-2.8.1 | HLR-1.1.1 | Functional |
| HLR-2.8.2 | HLR-2.8.1 | Functional |
| HLR-2.8.3 | HLR-2.8.2, HLR-2.7.3 | Functional |
| HLR-2.8.4 | HLR-2.7.5, HLR-2.7.4 | Functional |
| HLR-2.8.5 | HLR-1.1.4 | Functional |
| HLR-3.1.1 | HLR-2.3.1 | Functional |
| HLR-3.1.3 | HLR-3.1.1 | Functional |
| HLR-3.2.1 | HLR-2.3.1 | Functional |
| HLR-3.2.3 | HLR-3.2.1 | Functional |
| HLR-3.3.1 | HLR-2.3.1 | Functional |
| HLR-3.3.2 | HLR-3.3.1 | Functional |
| HLR-3.5.1 | HLR-3.1.1, HLR-3.2.1 | Functional |
| HLR-3.5.2 | HLR-3.5.1 | Functional |
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
| HLR-3.10.1 | HLR-0.1.1, HLR-0.1.2 | Functional |
| HLR-3.10.2 | HLR-3.10.1 | Functional |
| HLR-3.10.3 | HLR-3.10.1, HLR-5.1.1 | Functional |
| HLR-3.10.4 | HLR-3.10.1 | Functional |
| HLR-3.10.5 | HLR-3.10.1 | Functional |
| HLR-3.10.6 | HLR-3.10.1 | Functional |
| HLR-3.10.7 | HLR-3.10.3, HLR-5.1.1 | Functional |
| HLR-3.10.8 | HLR-3.10.1, HLR-5.1.1 | Functional |

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
