@echo off
REM ============================================
REM Boat IT - Release APK Build Script
REM Backend: https://boatsharing-backend.onrender.com
REM ============================================

echo.
echo ========================================
echo Building Boat IT Release APK
echo Backend: https://boatsharing-backend.onrender.com
echo ========================================
echo.

REM Clean previous builds
echo [1/3] Cleaning previous builds...
call gradlew.bat clean

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo.
echo [2/3] Building Release APK...
call gradlew.bat assembleRelease

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Build completed successfully!
echo.
echo ========================================
echo APK Location:
echo app\build\outputs\apk\release\app-release.apk
echo ========================================
echo.

REM Open the output folder
start "" "%CD%\app\build\outputs\apk\release"

echo.
echo Build completed! APK folder opened.
echo You can now share this APK with your client for testing.
echo.
pause
