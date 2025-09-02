# GenMe - AI Style Changer App

## 🚀 How to Run the App

### Prerequisites
1. **Android Studio**: Make sure you have Android Studio installed
2. **Java Development Kit (JDK)**: Android Studio should install this automatically

### Steps to Run:

1. **Open in Android Studio**:
   - Open Android Studio
   - Click "Open" and select this project folder
   - Wait for Gradle sync to complete

2. **Build the Project**:
   - In Android Studio, go to `Build` → `Clean Project`
   - Then go to `Build` → `Rebuild Project`

3. **Run the App**:
   - Connect an Android device or start an emulator
   - Click the green "Run" button or press `Shift + F10`

### 🛠️ Troubleshooting

If you're seeing the default template instead of your app:

1. **Ensure Compose is Enabled**: The `build.gradle.kts` file should have:
   ```kotlin
   buildFeatures {
       compose = true
       viewBinding = true
   }
   ```

2. **Check MainActivity**: Your `MainActivity.kt` should be using Compose (which it is!)

3. **Clean Build**: 
   - In Android Studio: `Build` → `Clean Project`
   - Then: `Build` → `Rebuild Project`

### 🎯 App Features
- **Landing Page**: AI Style Changer with two options
- **Clothes Change**: Upload your photo and clothes reference
- **Modern UI**: Beautiful gradients and animations

### 📱 App Structure
- `MainActivity.kt`: Main Compose activity
- `LandingPage.kt`: Home screen with feature selection
- `ClothesChangePage.kt`: Clothes change feature
- UI Theme in `ui/theme/` folder

### 🔧 Technical Details
- **Jetpack Compose**: Modern UI toolkit
- **Material Design 3**: Latest design system
- **Navigation Compose**: Screen navigation
- **Kotlin**: Programming language 