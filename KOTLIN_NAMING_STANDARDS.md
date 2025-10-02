# **KOTLIN NAMING STANDARDS**
## **Boat Sharing Application**

**Document ID:** NAMING-STANDARDS-001  
**Version:** 1.0  
**Date:** [Current Date]  
**Author:** Development Team  
**Reviewer:** Technical Lead  
**Approver:** Project Manager

---

## **1. INTRODUCTION**

### **1.1 Purpose**
This document establishes consistent naming conventions for the Boat Sharing Android application written in Kotlin. These standards ensure code readability, maintainability, and consistency across the entire codebase.

### **1.2 Scope**
This document applies to:
- All Kotlin source code files
- Data classes and structures
- API request/response models
- Database entities
- Configuration files
- Documentation and HLRs

### **1.3 Applicable Documents**
- Kotlin Coding Conventions (Official)
- Android Kotlin Style Guide
- Project Architecture Guidelines
- HLRs-SessionManagement.md

---

## **2. GENERAL PRINCIPLES**

### **2.1 Core Rules**
1. **Use meaningful names** that clearly express intent
2. **Avoid abbreviations** unless they are widely understood
3. **Be consistent** across similar contexts
4. **Follow Kotlin conventions** for readability
5. **Consider API compatibility** when naming public interfaces

### **2.2 Language Guidelines**
- Use **English** for all identifiers
- Use **descriptive names** over short names
- Avoid **Hungarian notation** (prefixes like `str`, `int`)
- Use **positive boolean names** (`isValid` not `isNotInvalid`)

---

## **3. NAMING CONVENTIONS**

### **3.1 Classes and Objects**

#### **Classes**
```kotlin
// ✅ PascalCase for class names
class SessionManager
class TokenRefreshService
class NetworkInterceptor
class GlobalSessionHandler

// ✅ PascalCase for data classes
data class UserData
data class TokenResponse
data class LoginRequest
data class ApiError
```

#### **Objects and Singletons**
```kotlin
// ✅ PascalCase for object names
object LogoutRequired : SessionEvent()
object SessionExpired : SessionEvent()
object TokenRefreshFailed : SessionEvent()

// ✅ PascalCase for companion objects
companion object {
    private const val MAX_RETRY_ATTEMPTS = 3
    private const val SESSION_TIMEOUT_MINUTES = 30L
}
```

#### **Sealed Classes**
```kotlin
// ✅ PascalCase for sealed class names
sealed class SessionEvent
sealed class NetworkResponse<T>
sealed class ApiResult<T>
```

### **3.2 Variables and Properties**

#### **Variables**
```kotlin
// ✅ camelCase for variables
val userId: String
val accessToken: String
val refreshToken: String
val lastActivityTime: Long
val sessionTimeoutMinutes: Long
val retryAttempts: Int
val maxRetryAttempts: Int
```

#### **Properties**
```kotlin
// ✅ camelCase for properties
var isLoggedIn: Boolean = false
var isSessionExpired: Boolean = false
var isTokenRefreshing: Boolean = false
var isMaintenanceMode: Boolean = false
```

#### **Constants**
```kotlin
// ✅ SCREAMING_SNAKE_CASE for constants
const val MAX_RETRY_ATTEMPTS = 3
const val SESSION_TIMEOUT_MINUTES = 30L
const val DEFAULT_TIMEOUT_SECONDS = 30
const val API_BASE_URL = "https://api.boatit.com"

// ✅ camelCase for private constants
private const val keyEmail = "email"
private const val keyUserId = "user_id"
private const val keyAccessToken = "access_token"
```

### **3.3 Functions and Methods**

#### **Function Names**
```kotlin
// ✅ camelCase for function names
fun handleLogin(userId: String?, userRole: String?)
fun refreshToken(): Boolean
fun updateLastActivity()
fun isSessionValid(): Boolean
fun performLogout()
fun clearUserData()
```

#### **Function Parameters**
```kotlin
// ✅ camelCase for parameters
fun saveLoginData(userData: UserData)
fun saveTokens(accessToken: String?, refreshToken: String?)
fun setCaptainStatus(isOnline: Boolean)
fun validateToken(token: String): Boolean
```

### **3.4 Data Structures**

#### **API Request/Response Models**
```kotlin
// ✅ Request models
data class RefreshRequest(
    val accessToken: String?,    // camelCase
    val refreshToken: String?    // camelCase
)

data class LoginRequest(
    val email: String,          // camelCase
    val password: String         // camelCase
)

// ✅ Response models
data class TokenResponse(
    val status: Int,            // camelCase
    val message: String,        // camelCase
    val obj: TokenData          // camelCase
)

data class LoginResponse(
    val status: Int,            // camelCase
    val message: String,       // camelCase
    val obj: UserData?         // camelCase
)
```

#### **Data Classes**
```kotlin
// ✅ All fields in camelCase
data class UserData(
    val email: String,
    val password: String,
    val userId: String,
    val username: String,
    val userRole: String,
    val missingStep: Int,
    val accessToken: String,
    val refreshToken: String
)

data class TokenData(
    val accessToken: String,    // camelCase
    val refreshToken: String    // camelCase
)

data class ApiError(
    val code: Int,             // camelCase
    val message: String,       // camelCase
    val details: String?,      // camelCase
    val timestamp: Long,       // camelCase
    val requestId: String?     // camelCase
)
```

### **3.5 Enums and Constants**

#### **Enums**
```kotlin
// ✅ PascalCase for enum names and values
enum class UserRole {
    CAPTAIN,    // PascalCase
    VOYAGER,    // PascalCase
    BUSINESS    // PascalCase
}

enum class SessionStatus {
    ACTIVE,     // PascalCase
    EXPIRED,    // PascalCase
    INACTIVE    // PascalCase
}
```

#### **Constants**
```kotlin
// ✅ SCREAMING_SNAKE_CASE for public constants
object ApiConstants {
    const val BASE_URL = "https://api.boatit.com"
    const val TIMEOUT_SECONDS = 30
    const val MAX_RETRY_ATTEMPTS = 3
}

// ✅ camelCase for private constants
class SessionManager {
    private val sessionTimeoutMinutes = 30L
    private val maxRetryAttempts = 3
}
```

---

## **4. SPECIFIC CONTEXTS**

### **4.1 Session Management**

#### **Session State Fields**
```kotlin
data class SessionState(
    val isLoggedIn: Boolean,           // camelCase with 'is' prefix
    val isSessionExpired: Boolean,     // camelCase with 'is' prefix
    val isTokenRefreshing: Boolean,    // camelCase with 'is' prefix
    val isMaintenanceMode: Boolean,    // camelCase with 'is' prefix
    val userId: String?,               // camelCase
    val userRole: String?,             // camelCase
    val lastActivityTime: Long,        // camelCase
    val sessionTimeoutMinutes: Long,   // camelCase
    val retryAttempts: Int,            // camelCase
    val maxRetryAttempts: Int          // camelCase
)
```

#### **Session Events**
```kotlin
sealed class SessionEvent {
    object LogoutRequired : SessionEvent()      // PascalCase
    object SessionExpired : SessionEvent()      // PascalCase
    object TokenRefreshFailed : SessionEvent()  // PascalCase
    object AccountDeactivated : SessionEvent()  // PascalCase
    object MaintenanceMode : SessionEvent()     // PascalCase
    object ForceLogout : SessionEvent()          // PascalCase
    object SessionRestored : SessionEvent()     // PascalCase
}
```

### **4.2 Network Layer**

#### **API Models**
```kotlin
// ✅ Request models
data class RefreshRequest(
    val accessToken: String?,    // camelCase
    val refreshToken: String?    // camelCase
)

// ✅ Response models
data class TokenResponse(
    val status: Int,            // camelCase
    val message: String,        // camelCase
    val obj: TokenData          // camelCase
)

// ✅ Generic response wrapper
sealed class NetworkResponse<T> {
    class Success<T>(val data: T) : NetworkResponse<T>()     // PascalCase
    class Error<T>(val message: String) : NetworkResponse<T>() // PascalCase
    class Loading<T> : NetworkResponse<T>()                   // PascalCase
}
```

### **4.3 Preference Management**

#### **SharedPreferences Keys**
```kotlin
companion object {
    // ✅ SCREAMING_SNAKE_CASE for preference keys
    private const val KEY_EMAIL = "email"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_USER_ROLE = "userrole"
    private const val KEY_STEP = "step"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_CAPTAIN_STATUS = "captain_status"
}
```

---

## **5. ANTI-PATTERNS TO AVOID**

### **5.1 Incorrect Naming**

#### **❌ Don't Use These Patterns**
```kotlin
// ❌ PascalCase for variables
val UserId: String
val AccessToken: String

// ❌ lowercase for classes
class sessionmanager
class tokenrefreshservice

// ❌ Mixed conventions
data class UserData(
    val Email: String,        // ❌ PascalCase
    val password: String,     // ✅ camelCase
    val UserId: String        // ❌ PascalCase
)

// ❌ Abbreviations
val usrId: String
val accTok: String
val refTok: String

// ❌ Hungarian notation
val strUserId: String
val intRetryCount: Int
val boolIsLoggedIn: Boolean

// ❌ Negative boolean names
val isNotExpired: Boolean    // ❌ Use isExpired instead
val hasNoErrors: Boolean     // ❌ Use hasErrors instead
```

### **5.2 Common Mistakes**

#### **API Response Fields**
```kotlin
// ❌ Wrong - PascalCase in API models
data class TokenResponse(
    val Status: Int,          // ❌ Should be status
    val Message: String,      // ❌ Should be message
    val Accesstoken: String   // ❌ Should be accessToken
)

// ✅ Correct - camelCase in API models
data class TokenResponse(
    val status: Int,          // ✅ camelCase
    val message: String,     // ✅ camelCase
    val accessToken: String  // ✅ camelCase
)
```

---

## **6. DOCUMENTATION STANDARDS**

### **6.1 HLRs Documentation**

#### **Data Structure Tables**
```markdown
| Type | Name | Description |
|------|------|-------------|
| String | accessToken | New access token received from refresh operation |
| String | refreshToken | New refresh token received from refresh operation |
| Int | status | HTTP status code of the response |
| String | message | Response message from the server |
```

#### **Function Documentation**
```kotlin
/**
 * Handles user login and initializes session state
 * @param userId Unique identifier for the user
 * @param userRole Role of the user (captain, voyager, business)
 */
fun handleLogin(userId: String?, userRole: String?) {
    // Implementation
}
```

### **6.2 Code Comments**

#### **Class Documentation**
```kotlin
/**
 * Centralized session manager that handles all session-related operations
 * including login, logout, token refresh, and session state management
 */
class SessionManager(
    private val tokenProvider: TokenProvider,
    private val tokenRefreshService: TokenRefreshService
) : ViewModel()
```

#### **Function Documentation**
```kotlin
/**
 * Validates if the current session is still active and not expired
 * @return true if session is valid, false otherwise
 */
fun isSessionValid(): Boolean {
    val currentTime = System.currentTimeMillis()
    val timeoutMillis = sessionTimeoutMinutes * 60 * 1000
    return isLoggedIn && !isSessionExpired && (currentTime - lastActivityTime) < timeoutMillis
}
```

---

## **7. IMPLEMENTATION GUIDELINES**

### **7.1 Code Review Checklist**

#### **Naming Standards Checklist**
- [ ] All classes use PascalCase
- [ ] All variables and properties use camelCase
- [ ] All functions use camelCase
- [ ] All constants use SCREAMING_SNAKE_CASE
- [ ] All enum values use PascalCase
- [ ] All API models use camelCase for fields
- [ ] All boolean variables use positive naming (`isValid` not `isNotInvalid`)
- [ ] All names are descriptive and meaningful
- [ ] No abbreviations unless widely understood
- [ ] Consistent naming across similar contexts

### **7.2 IDE Configuration**

#### **Recommended IDE Settings**
- Enable **Kotlin code style** enforcement
- Use **camelCase** for variables and functions
- Use **PascalCase** for classes and objects
- Use **SCREAMING_SNAKE_CASE** for constants
- Enable **unused import** detection
- Enable **unused variable** detection

---

## **8. EXAMPLES**

### **8.1 Complete Data Class Example**
```kotlin
/**
 * Data class representing user authentication information
 */
data class UserData(
    val email: String,           // camelCase
    val password: String,        // camelCase
    val userId: String,          // camelCase
    val username: String,        // camelCase
    val userRole: String,        // camelCase
    val missingStep: Int,        // camelCase
    val accessToken: String,     // camelCase
    val refreshToken: String     // camelCase
) {
    companion object {
        private const val DEFAULT_MISSING_STEP = 0  // SCREAMING_SNAKE_CASE
    }
    
    /**
     * Checks if user has completed onboarding
     * @return true if onboarding is complete
     */
    fun isOnboardingComplete(): Boolean {  // camelCase function
        return missingStep == 0
    }
}
```

### **8.2 Complete Service Class Example**
```kotlin
/**
 * Service responsible for managing authentication tokens
 */
class TokenRefreshService(
    private val apiService: ApiService,
    private val tokenProvider: TokenProvider
) {
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3  // SCREAMING_SNAKE_CASE
        private const val RETRY_DELAY_MS = 1000L  // SCREAMING_SNAKE_CASE
    }
    
    private var retryAttempts = 0  // camelCase
    
    /**
     * Attempts to refresh the access token using the refresh token
     * @return true if refresh was successful, false otherwise
     */
    suspend fun refreshToken(): Boolean {  // camelCase function
        return try {
            val refreshToken = tokenProvider.getRefreshToken()  // camelCase
            val response = apiService.refreshToken(refreshToken)  // camelCase
            
            if (response.isSuccessful) {
                val tokenData = response.body()?.obj  // camelCase
                tokenData?.let {
                    tokenProvider.saveTokens(it.accessToken, it.refreshToken)  // camelCase
                    retryAttempts = 0  // camelCase
                    true
                } ?: false
            } else {
                handleRefreshFailure()  // camelCase
                false
            }
        } catch (e: Exception) {
            handleRefreshFailure()  // camelCase
            false
        }
    }
    
    private fun handleRefreshFailure() {  // camelCase function
        retryAttempts++  // camelCase
        if (retryAttempts >= MAX_RETRY_ATTEMPTS) {
            // Trigger logout
        }
    }
}
```

---

## **9. CHANGE CONTROL**

### **9.1 Updates to Standards**
Any changes to naming standards shall follow this process:
1. **Proposal**: Submit naming standard change request
2. **Review**: Technical team reviews impact
3. **Approval**: Technical lead approves changes
4. **Implementation**: Update codebase and documentation
5. **Verification**: Code review ensures compliance

### **9.2 Compliance Monitoring**
- **Code Reviews**: All code must comply with naming standards
- **Automated Checks**: IDE and linting tools enforce standards
- **Documentation Updates**: HLRs must reflect current naming conventions
- **Regular Audits**: Quarterly review of naming compliance

---

## **10. APPENDICES**

### **Appendix A: Quick Reference**

| Element | Convention | Example |
|---------|------------|---------|
| **Classes** | PascalCase | `SessionManager` |
| **Objects** | PascalCase | `LogoutRequired` |
| **Variables** | camelCase | `userId` |
| **Properties** | camelCase | `isLoggedIn` |
| **Functions** | camelCase | `handleLogin()` |
| **Constants** | SCREAMING_SNAKE_CASE | `MAX_RETRY_ATTEMPTS` |
| **Enum Values** | PascalCase | `CAPTAIN` |
| **API Fields** | camelCase | `accessToken` |

### **Appendix B: Common Patterns**

#### **Boolean Naming**
```kotlin
// ✅ Positive boolean names
val isValid: Boolean
val isLoggedIn: Boolean
val hasErrors: Boolean
val canRetry: Boolean

// ❌ Negative boolean names
val isNotValid: Boolean
val isNotLoggedIn: Boolean
val hasNoErrors: Boolean
val cannotRetry: Boolean
```

#### **Collection Naming**
```kotlin
// ✅ Plural names for collections
val users: List<User>
val tokens: Set<String>
val errors: MutableList<Error>

// ✅ Descriptive names
val activeSessions: List<Session>
val expiredTokens: Set<String>
val validationErrors: List<String>
```

---

**Document Control:**
- **Status:** Active
- **Distribution:** Development Team, QA Team, Technical Lead
- **Next Review Date:** [Date + 90 days]
- **Approval Required:** Technical Lead, Project Manager
