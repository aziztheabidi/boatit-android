# CODE REVIEW CHECKLIST - BOAT SHARING APP

## **CRITICAL ISSUES TO FIX**

### **1. MONOLITHIC COMPOSABLES**

#### **BusinessDashboard.kt**
- **Lines 1-1114**: Break into smaller composables (navigation, stats, charts, actions)
- **Lines 200-400**: Extract voyage management to separate composable
- **Lines 500-700**: Extract analytics dashboard to separate composable
- **Lines 800-1000**: Extract business actions to separate composable

#### **DashboardScreen.kt**
- **Lines 1-522**: Break into smaller composables (map, filters, voyage list)
- **Lines 100-200**: Extract map composable
- **Lines 300-400**: Extract voyage list composable
- **Lines 500-522**: Extract booking flow composable

#### **CreateVoyageSponsor.kt**
- **Lines 1-476**: Break into smaller composables (form, payment, validation)
- **Lines 200-300**: Extract payment composable
- **Lines 400-476**: Extract validation composable

#### **CreateVoyageRateCalc.kt**
- **Lines 1-460**: Break into smaller composables (form, calculations, results)
- **Lines 300-400**: Extract calculation composable
- **Lines 400-460**: Extract results composable

#### **CreateVoyage.kt**
- **Lines 1-378**: Break into smaller composables (form, date picker, validation)
- **Lines 200-300**: Extract date picker composable
- **Lines 300-378**: Extract validation composable

#### **ConfirmVoyage.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-547**: Break into smaller composables (form, payment, validation)
- **Lines 98-117**: Extract form state management (16 state variables)
- **Lines 200-400**: Extract payment form composable
- **Lines 400-500**: Extract validation logic composable
- **Lines 500-547**: Extract confirmation dialog composable

#### **BusinessAccountInfoScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-488**: Break into smaller composables (form, validation, navigation)
- **Lines 101-116**: Extract form state management (16 state variables)
- **Lines 200-300**: Extract business form composable
- **Lines 300-400**: Extract validation logic composable
- **Lines 400-488**: Extract navigation logic composable

#### **UserAccountInfoScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-506**: Break into smaller composables (form, validation, navigation)
- **Lines 98-116**: Extract form state management (16 state variables)
- **Lines 200-300**: Extract user form composable
- **Lines 300-400**: Extract validation logic composable
- **Lines 400-506**: Extract navigation logic composable

#### **ChatScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-300**: Break into smaller composables (message list, input, dialogs)
- **Lines 78-82**: Extract message state management
- **Lines 180-200**: Extract message item composable
- **Lines 200-250**: Extract complaint dialog composable

#### **FindDestinationLocationScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-347**: Break into smaller composables (search, autocomplete, selection)
- **Lines 76-86**: Extract location state management (8 state variables)
- **Lines 100-200**: Extract autocomplete composable
- **Lines 200-300**: Extract location selection composable

#### **BusinessListScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-345**: Break into smaller composables (list, tabs, notifications)
- **Lines 96-103**: Extract list state management (7 state variables)
- **Lines 200-300**: Extract business list composable
- **Lines 300-345**: Extract notification handling composable

#### **CaptainAccountInfoScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-506**: Break into smaller composables (form, validation, navigation)
- **Lines 84-98**: Extract form state management (16 state variables)
- **Lines 200-300**: Extract captain form composable
- **Lines 300-400**: Extract validation logic composable
- **Lines 400-506**: Extract navigation logic composable

#### **VoyagersListScreen.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-421**: Break into smaller composables (list, search, tabs)
- **Lines 73-79**: Extract list state management (6 state variables)
- **Lines 200-300**: Extract voyager list composable
- **Lines 300-400**: Extract search functionality composable

#### **FindBoat.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-538**: Break into smaller composables (form, dropdowns, validation)
- **Lines 79-91**: Extract form state management (12 state variables)
- **Lines 200-300**: Extract location form composable
- **Lines 300-400**: Extract passenger selection composable
- **Lines 400-500**: Extract validation logic composable

#### **BusinessDetail.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-415**: Break into smaller composables (details, follow, gallery)
- **Lines 73-81**: Extract detail state management (9 state variables)
- **Lines 200-300**: Extract business details composable
- **Lines 300-400**: Extract follow functionality composable

#### **AddGeneralBusinessInfo.kt** ⚠️ **NEWLY IDENTIFIED**
- **Lines 1-482**: Break into smaller composables (form, validation, navigation)
- **Lines 96-101**: Extract form state management (6 state variables)
- **Lines 200-300**: Extract business info form composable
- **Lines 300-400**: Extract validation logic composable
- **Lines 400-482**: Extract navigation logic composable

---

### **2. MASSIVE DEPENDENCY INJECTION MODULE**

#### **Modules.kt**
- **Lines 1-257**: Split into feature-specific modules
- **Lines 50-100**: Extract ViewModel module
- **Lines 100-150**: Extract Repository module
- **Lines 150-200**: Extract Firebase module
- **Lines 200-257**: Extract Google services module

---

### **3. NO NETWORK RETRY LOGIC**

#### **FindBoatRepo.kt**
- **Lines 17-33**: Add retry logic with exponential backoff

#### **BookVoyageRepo.kt**
- **Lines 17-35**: Add retry logic and remove debug prints

#### **LoginRepo.kt**
- **Lines 17-33**: Add retry logic for network failures

#### **FetchNearByVoyagesRepo.kt**
- **Lines 16-29**: Add retry logic and remove println()

#### **CalculateFairRepo.kt**
- **Lines 18-44**: Add retry logic and remove debug logs

---

### **4. NO TOKEN REFRESH MECHANISM**

#### **NearByVoyagesViewModel.kt**
- **Lines 49-55**: Implement token refresh instead of logging out user

#### **GetBusinessViewModel.kt**
- **Lines 53-58**: Implement token refresh instead of logging out user

#### **GetActiveVoyageViewModel.kt**
- **Lines 45-49**: Add 401 handling and remove debug logs

#### **FindBoatViewModel.kt**
- **Lines 30-33**: Add 401 handling and remove debug logs

#### **RoleViewModel.kt**
- **Lines 40-52**: Implement token refresh logic

---

### **5. EXCESSIVE MAGIC NUMBERS & STRINGS**

#### **SplashComposable.kt**
- **Line 49**: Extract logo size to constants
- **Line 61**: Extract splash delay to constants
- **Lines 63, 69, 79**: Replace role strings with enums

#### **Dashboardscreen.kt**
- **Line 113**: Extract coordinates to constants
- **Line 117**: Extract zoom level to constants
- **Line 134**: Extract default text to constants

#### **CalculateFairViewModel.kt**
- **Line 121**: Extract division number (60f) to constants
- **Line 124**: Extract default value (0.0) to constants
- **Lines 131-133**: Extract default values to constants

#### **generalSignupViewModel.kt**
- **Lines 45-49**: Extract validation lengths to constants
- **Line 94**: Extract error message to constants

#### **Constants.kt**
- **Line 9**: Extract URL to configuration
- **Line 15**: Extract list size (24) to constants
- **Line 19**: Extract dock type string to enums
- **Lines 24-28**: Extract default values to constants
- **Line 30**: Extract time string to constants
- **Lines 47, 53, 58-62, 64**: Remove duplicate hardcoded values

---

### **6. BUSINESS LOGIC MIXED WITH VIEW LOGIC**

#### **CalculateFairViewModel.kt**
- **Lines 112-147**: Extract time calculations to dedicated service

#### **BusinessAccountInfoScreen.kt**
- **Line 109**: Move email validation to ViewModel
- **Lines 118-124**: Move form validation to ViewModel
- **Lines 134-149**: Move navigation logic to ViewModel

#### **UserAccountInfoScreen.kt**
- **Line 109**: Move email validation to ViewModel
- **Lines 119-125**: Move form validation to ViewModel
- **Lines 135-149**: Move navigation logic to ViewModel

#### **CalculateFairRepo.kt**
- **Lines 18-42**: Extract parameter mapping to dedicated service

---

### **7. EXCESSIVE REMEMBER USAGE**

**Categorization Guide:**
- **MOVE TO VIEWMODEL**: Business data, form data, loading states, error states, navigation state
- **KEEP IN COMPOSABLE**: UI interaction state, focus management, derived UI lists, visual states

#### **BusinessDashboard.kt**
- **Lines 130-133**: Move business data (BDetail, shores, zones, island) to ViewModel
- **Lines 134-136**: Move form selections (zone, shore, islnd) to ViewModel
- **Lines 137-138**: Move form data (selectedOption, businessDescription) to ViewModel
- **Lines 140-144**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, isNetworkError) to ViewModel
- **Lines 149-152**: Keep UI state (expanded, expandeds, expandedi, isEditing) in Composable
- **Lines 123, 125-127, 129, 153-155**: Keep UI-specific state (focusRequester, sheetState, coroutineScope, editableList) in Composable

#### **Dashboardscreen.kt**
- **Lines 120-125**: Move payment data (paymentIntentClientSecret, publishableKey, id, PaymentIntentid, ephemeralKeySecret) to ViewModel
- **Line 125**: Move voyage data (voyageDetail) to ViewModel
- **Lines 127-130**: Move navigation state (showFindBoat, showConfirmBooking, showStartBooking, showVoyageDetails) to ViewModel
- **Lines 135-139**: Move error states (isError, errorMessage, showWaitingResponsePrompt, waitingResponsePromptValue, showBottomSheet) to ViewModel
- **Lines 145-147**: Move sheet state (currentSheetTarget) to ViewModel
- **Lines 112, 114, 126, 131-134**: Keep UI state (selectedLocation, currentLatLng, isMenuIconVisible, pickupLocation, dropOffLocation, totalPassengers, date) in Composable

#### **BusinessAccountInfoScreen.kt**
- **Lines 101-106**: Move form data (firstName, lastName, phoneNumber, address, dob, paypalEmail) to ViewModel
- **Line 108**: Move booking data (bookingDate) to ViewModel
- **Lines 110-115**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, getingData, isNetworkError) to ViewModel

#### **UserAccountInfoScreen.kt**
- **Lines 98-103**: Move form data (firstName, lastName, phoneNumber, address, dob, paypalEmail) to ViewModel
- **Line 105**: Move booking data (bookingDate) to ViewModel
- **Lines 110-116**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, getingData, isNetworkError) to ViewModel

#### **CreateVoyageSponsor.kt**
- **Lines 96-98**: Move form data (findBoat, dob, paypalEmail) to ViewModel
- **Lines 99-100**: Move dialog state (showDialog, showErrorDialog) to ViewModel
- **Lines 104-110**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, getingData, isNetworkError) to ViewModel
- **Line 118**: Move error text (responseErrorText) to ViewModel

#### **CreateVoyageRateCalc.kt**
- **Lines 74-79**: Move form data (firstName, lastName, phoneNumber, address, dob, paypalEmail) to ViewModel
- **Lines 85-90**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, getingData, isNetworkError) to ViewModel

#### **Sponsor.kt**
- **Lines 88-93**: Move form data (firstName, lastName, phoneNumber, address, dob, paypalEmail) to ViewModel
- **Line 95**: Move booking data (bookingDate) to ViewModel
- **Lines 98-105**: Move loading states (isError, errorMessage, isButtonEnabled, isLoading, getingData, isNetworkError, isChecked) to ViewModel

#### **VoyagerFeedbackScreen.kt**
- **Lines 56-62**: Move feedback data (title, reviewText, rating, isLoading, isNetworkError) to ViewModel

#### **VoyageBookedVoyager.kt**
- **Lines 71-83**: Move voyage data (title, isButtonEnabled, isLoading, isNetworkError, errorMessage, paymentIntentClientSecret, publishableKey, id, PaymentIntentid, ephemeralKeySecret) to ViewModel

#### **FindBoat.kt**
- **Lines 79-90**: Move form data (showDialog, pLocation, dLocation, category, noOffPassengers, bookingDate, isError, errorMessage, expanded, expandedp, expandedd) to ViewModel

#### **BusinessDetail.kt**
- **Lines 73-80**: Move business data (selectedOption, businessDescription, isError, errorMessage, isButtonEnabled, isLoading, isNetworkError, showDialog) to ViewModel

---

### **8. INCONSISTENT NAMING SCHEMA**

#### **Constants.kt**
- **Lines 14-22**: Fix inconsistent naming (Cates, Business, BusinessDock, BusinessDockTYpe, PLACES, BPLACES, sponsorList)
- **Line 19**: Fix typo "BusinessDockTYpe" → "BusinessDockType"

#### **BusinessDashboard.kt**
- **Lines 150-151**: Fix inconsistent naming (expandeds, expandedi) → (isShoreExpanded, isIslandExpanded)
- **Lines 130-133**: Fix inconsistent naming (BDetail, shores, zones, island) → (businessDetail, shoreList, zoneList, islandList)

#### **Dashboardscreen.kt**
- **Lines 120-125**: Fix inconsistent naming (paymentIntentClientSecret, publishableKey, PaymentIntentid, ephemeralKeySecret)
- **Lines 127-130**: Fix inconsistent naming (showFindBoat, showConfirmBooking, showStartBooking, showVoyageDetails)

#### **FindBoat.kt**
- **Lines 89-90**: Fix inconsistent naming (expandedp, expandedd) → (isPickupExpanded, isDropoffExpanded)
- **Lines 81-84**: Fix inconsistent naming (pLocation, dLocation, noOffPassengers) → (pickupLocation, dropoffLocation, numberOfPassengers)

#### **All Files with "getingData"**
- **Lines in multiple files**: Fix typo "getingData" → "isGettingData" (65 instances across 15 files)
  - Sponsor.kt (Line 103)
  - FutureVoyages.kt (Lines 76, 82, 88, 97, 98, 105, 117, 127, 132, 151, 243)
  - CreateVoyageRateCalc.kt (Line 89)
  - CreateVoyageSponsor.kt (Line 109)
  - UserAccountInfoScreen.kt (Lines 115, 164, 171, 176, 181, 207)
  - CaptainAccountInfoScreen.kt (Lines 97, 142, 149, 153, 158)
  - AddCaptainDocumentInfoScreen.kt (Lines 67, 91, 92, 96, 98)
  - AddCaptainBoatInfoScreen.kt (Lines 70, 76, 78, 82, 87)
  - AddBusinessDescriptions.kt (Lines 82, 118, 126, 130, 131, 143)
  - BusinessAccountInfoScreen.kt (Lines 114, 160, 167, 172, 177, 189)
  - AddBusinessLogo.kt (Lines 95, 135, 140, 144, 145, 177)
  - AddGeneralBusinessInfo.kt (Lines 101, 142, 143, 147, 149, 162)
  - ConfirmVoyage.kt (Lines 115, 161, 168, 172, 177, 189)

#### **Class Naming Issues**
- **Prefmanager.kt**: Fix class name "SharedPrefManager" → "SharedPreferenceManager"
- **Businesslogoviewmodel.kt**: Fix class name "BusinessLogoViewModel" → "BusinessLogoViewModel"
- **generalSignupViewModel.kt**: Fix class name "RegistrationViewModel" → "RegistrationViewModel"
- **generalSignUpRepo.kt**: Fix class name "RegistrationRepository" → "RegistrationRepository"

#### **Function Naming Issues**
- **CalculateFairRepo.kt**: Fix function name "CalculateFairRepoFunc" → "calculateFair"
- **SponsorPaymentSheetRepository.kt**: Fix function name "SheetConfi" → "configurePaymentSheet"
- **generalSignUpRepo.kt**: Fix function name "tempRegister" → "registerUser"

#### **Variable Naming Issues**
- **Multiple files**: Fix inconsistent boolean naming (isError, isButtonEnabled, isLoading, isNetworkError)
- **Multiple files**: Fix inconsistent string naming (errorMessage, bookingDate, paypalEmail)
- **Multiple files**: Fix inconsistent object naming (voyageDetail, businessDescription, selectedOption)

---
1. **HIGH PRIORITY**: Network retry logic (Section 3)
2. **HIGH PRIORITY**: Token refresh mechanism (Section 4)
3. **MEDIUM PRIORITY**: Magic numbers & strings (Section 5)
4. **MEDIUM PRIORITY**: Business logic separation (Section 6)
5. **MEDIUM PRIORITY**: Excessive remember usage (Section 7)
6. **MEDIUM PRIORITY**: Monolithic composables (Section 1) - **NOW 17 FILES** ⚠️
7. **LOW PRIORITY**: Inconsistent naming schema (Section 8)
8. **LOW PRIORITY**: DI module splitting (Section 2)

---

## **QUICK WINS**

- **Remove debug prints**: Lines in BookVoyageRepo.kt, FetchNearByVoyagesRepo.kt, CalculateFairRepo.kt
- **Extract constants**: All magic numbers and strings listed above
- **Add retry logic**: All repository files listed above
- **Implement token refresh**: All ViewModel files listed above
- **Move form data to ViewModels**: All remember instances in signup screens
- **Move loading states to ViewModels**: All isLoading, isError, isNetworkError instances
- **Fix "getingData" typo**: 65 instances across 15 files
- **Fix inconsistent variable names**: expandeds, expandedi, expandedp, expandedd

---

## **ESTIMATED EFFORT**

- **Network retry logic**: 2-3 days
- **Token refresh mechanism**: 3-4 days
- **Magic numbers & strings**: 1-2 days
- **Business logic separation**: 4-5 days
- **Excessive remember usage**: 3-4 days
- **Inconsistent naming schema**: 2-3 days
- **Monolithic composables**: 3-4 weeks (**NOW 17 FILES** ⚠️)
- **DI module splitting**: 3-5 days

**Total estimated effort: 7-8 weeks** ⚠️ **SIGNIFICANTLY INCREASED DUE TO 17 MONOLITHIC FILES**
