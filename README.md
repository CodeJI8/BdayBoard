# 🎂 Bday Board

A clean, minimal birthday tracker for Android. Store people's birthdays and see who's next — no notifications, no accounts, no backend. Just a beautiful list that auto-sorts by the nearest upcoming birthday.

## Features

- **Add birthdays** — name (required), day, month, and optional birth year
- **Edit & delete** — long-press to edit, swipe left to delete (with confirmation)
- **Smart countdown** — "Today 🎂", "Tomorrow", "in 23 days"
- **Age tracking** — shows the age they'll turn (only when birth year is provided)
- **Auto-sorted** — always ordered by nearest upcoming birthday; today's birthdays pinned at top
- **Search** — filter the list by name
- **Feb 29 handling** — treated as Feb 28 on non-leap years
- **Light & dark mode** — follows system theme with Material You dynamic colors on Android 12+
- **Fully offline** — data stored locally with Room database

## Screenshots

| Empty State | Birthday List | Add Birthday |
|:-:|:-:|:-:|
| 🎂 friendly empty state | Sorted cards with color badges | Simple day/month/year form |

## Tech Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **Room** for local SQLite persistence
- **MVVM** architecture with `ViewModel` + `StateFlow`
- **KSP** for Room annotation processing
- **Edge-to-edge** UI with `enableEdgeToEdge()`

## Project Structure

```
app/src/main/java/com/wishINC/bdayboard/
├── data/
│   ├── Birthday.kt              # Room entity
│   ├── BirthdayDao.kt           # Data access object
│   ├── BirthdayDatabase.kt      # Room database singleton
│   └── BirthdayRepository.kt    # Repository layer
├── ui/
│   ├── components/
│   │   ├── AddEditBirthdayDialog.kt  # Add/edit form dialog
│   │   ├── BirthdayCard.kt          # List item with swipe-to-delete
│   │   ├── DeleteConfirmDialog.kt   # Delete confirmation
│   │   ├── EmptyState.kt           # Friendly empty state
│   │   └── NextBirthdayCard.kt     # Hero card for nearest birthday
│   ├── screens/
│   │   └── HomeScreen.kt           # Main screen
│   └── theme/
│       ├── Color.kt                 # Color palette
│       ├── Theme.kt                 # Material theme setup
│       └── Type.kt                  # Typography
├── util/
│   └── DateUtils.kt                # Date calculation logic
├── viewmodel/
│   └── BirthdayViewModel.kt        # ViewModel with search & sorting
├── BdayBoardApplication.kt         # Application class
└── MainActivity.kt                 # Entry point
```

## Setup & Build

### Prerequisites
- Android Studio (Ladybug or newer recommended)
- JDK 11+
- Android SDK with API 37 (or adjust `compileSdk`/`targetSdk`)

### Steps

1. **Clone / open** the project in Android Studio
2. **Sync Gradle** — Android Studio will download all dependencies automatically
3. **Run** on an emulator or physical device (min API 26 / Android 8.0)

```bash
# Or build from command line:
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Running Tests

```bash
./gradlew test
```

Unit tests for date logic are in [`DateUtilsTest.kt`](app/src/test/java/com/wishINC/bdayboard/util/DateUtilsTest.kt), covering:
- Birthday later this year / today / already passed
- Feb 29 on leap year vs non-leap year
- Year rollover (Dec → Jan)
- Days-until calculation
- Turning age with and without birth year
- Sort order verification

## Design Details

- **Color badges**: 🔴 Today (accent), 🟠 Within 7 days (warm), ⚪ Later (neutral)
- **Next Birthday card**: Highlighted hero card at the top with large text
- **Swipe to delete**: Swipe left reveals red delete background; requires confirmation dialog
- **Long press to edit**: Opens the same form pre-filled with existing data

## License

MIT
