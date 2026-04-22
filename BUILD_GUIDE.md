# Boat IT - Release Build Guide

## 🚀 Backend Configuration

**Backend URL:** https://boatsharing-backend.onrender.com  
**API Base URL:** https://boatsharing-backend.onrender.com/api  
**Status:** ✅ Live on Render

---

## 📱 Building Release APK

### Method 1: Using Build Script (Easiest)

1. Open Command Prompt in the `james_boat` folder
2. Run the build script:
   ```cmd
   build-release.bat
   ```
3. Wait for build to complete (5-10 minutes)
4. APK will be generated at: `app\build\outputs\apk\release\app-release.apk`

### Method 2: Manual Build

1. Open Command Prompt in the `james_boat` folder
2. Clean previous builds:
   ```cmd
   gradlew.bat clean
   ```
3. Build release APK:
   ```cmd
   gradlew.bat assembleRelease
   ```
4. Find APK at: `app\build\outputs\apk\release\app-release.apk`

---

## 📦 APK Details

- **App Name:** Boat IT
- **Package:** com.boatit.boat_it
- **Version:** 1.0 (versionCode: 1)
- **Min Android:** 7.0 (API 24)
- **Target Android:** 14 (API 34)
- **Backend:** Render (https://boatsharing-backend.onrender.com)

---

## 🧪 Testing Instructions for Client

### Installation
1. Download the APK file
2. Enable "Install from Unknown Sources" on Android device
3. Install the APK
4. Open Boat IT app

### Test Scenarios

#### 1. User Registration & Login
- ✅ Create new account (Voyager/Captain/Business)
- ✅ Email verification
- ✅ Login with credentials
- ✅ Forgot password flow

#### 2. Voyager Features
- ✅ Find boat/destination
- ✅ Book voyage
- ✅ View active voyages
- ✅ View past voyages
- ✅ Payment processing (Stripe)
- ✅ Follow businesses
- ✅ Submit feedback

#### 3. Captain Features
- ✅ Update availability status
- ✅ Accept/Decline voyage requests
- ✅ Start voyage
- ✅ Complete voyage
- ✅ View active voyages
- ✅ View past voyages
- ✅ Submit feedback

#### 4. Business Features
- ✅ Business profile setup
- ✅ Add business information
- ✅ Upload logo and images
- ✅ View dashboard
- ✅ Manage relationships

---

## ⚠️ Important Notes

### Backend Limitations (Render Free Tier)
- **Cold Start:** First request after 15 minutes of inactivity takes 30-60 seconds
- **Solution:** Wait for backend to wake up, then retry
- **Recommendation:** Upgrade to paid tier ($7/month) for production

### Testing Tips
1. **First Launch:** Backend might be sleeping - wait 1 minute and retry
2. **Network:** Ensure stable internet connection
3. **Permissions:** Grant all required permissions (Location, Storage, etc.)
4. **GPS:** Enable location services for map features

### Known Issues
- Backend cold start delay on first request (Render free tier limitation)
- File uploads stored temporarily (use cloud storage for production)

---

## 🔧 Build Configuration

### Debug Build (Development)
- Backend: Local (http://192.168.10.15:8080)
- Use for local testing only

### Release Build (Production)
- Backend: Render (https://boatsharing-backend.onrender.com)
- Use for client testing and production

---

## 📊 Backend Health Check

Test if backend is live:
- **Swagger UI:** https://boatsharing-backend.onrender.com/swagger
- **Health Check:** https://boatsharing-backend.onrender.com/swagger/index.html

If backend is sleeping:
1. Open Swagger URL in browser
2. Wait 30-60 seconds for backend to wake up
3. Retry app operations

---

## 🐛 Troubleshooting

### Build Fails
**Error:** "SDK not found" or "Java not found"
- **Solution:** Install Android Studio and set ANDROID_HOME environment variable

**Error:** "Gradle sync failed"
- **Solution:** Run `gradlew.bat clean` and retry

### App Crashes on Launch
**Issue:** App crashes immediately
- **Solution:** Check if all permissions are granted
- **Solution:** Clear app data and reinstall

### Network Errors
**Issue:** "Connection failed" or "Timeout"
- **Solution:** Check internet connection
- **Solution:** Wait for backend to wake up (cold start)
- **Solution:** Verify backend is live at Swagger URL

### Login Fails
**Issue:** "Invalid credentials" or "Server error"
- **Solution:** Ensure backend is awake (check Swagger)
- **Solution:** Verify email/password are correct
- **Solution:** Try forgot password flow

---

## 📞 Support

For issues or questions:
1. Check backend status at Swagger URL
2. Review logs in Android Studio Logcat
3. Contact development team with error details

---

## 🎯 Next Steps for Production

### Recommended Upgrades:
1. **Render Backend:** Upgrade to paid tier ($7/month) for:
   - No cold starts
   - Better performance
   - Custom domain support

2. **File Storage:** Integrate cloud storage:
   - AWS S3
   - Azure Blob Storage
   - Cloudinary

3. **Stripe:** Switch to live keys for real payments

4. **Play Store:** Prepare for Google Play Store release:
   - Generate signed APK with keystore
   - Create app listing
   - Add privacy policy
   - Submit for review

---

## 📝 Version History

### v1.0 (Current)
- Initial release build
- Connected to Render backend
- All core features implemented
- Ready for client testing

---

**Build Date:** April 17, 2026  
**Backend:** Render (Live)  
**Status:** Ready for Testing ✅
