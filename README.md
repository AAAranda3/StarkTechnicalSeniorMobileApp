# Stark VARG Telemetry — Senior Android Engineering

Android mobile application displaying real-time and static telemetry snapshots from the **Stark VARG MX 1.2** electric motorcycle, featuring Clean Architecture, Jetpack Compose Material 3 dark-cockpit design, interactive power mode profiles, live powertrain charging simulation, and full-bleed skeleton loading.

---

## Architecture

Built adhering to **Clean Architecture** separated into three distinct layers:

```
app/src/main/kotlin/com/stark/varg/
├── data/
│   ├── dto/TelemetryDto.kt              # Moshi DTOs with KSP codegen annotations
│   ├── source/TelemetryDataSource.kt    # Interface contract for data fetching
│   ├── source/LocalTelemetryDataSource.kt# Asset-based CAN-bus synchronization provider
│   ├── mapper/TelemetryMapper.kt        # DTO → Domain transformation (isolated & tested)
│   └── repository/TelemetryRepositoryImpl.kt # Single source of truth implementation
├── domain/
│   ├── model/                           # Pure Kotlin immutable business models
│   │   ├── TelemetrySnapshot.kt         # Aggregate Root
│   │   ├── BatteryStatus.kt             # SoC %, range, temperature, charging state
│   │   ├── ChargingState.kt             # CHARGING, DISCHARGING, UNKNOWN
│   │   ├── MotorStatus.kt               # Power HP and operating temperature
│   │   ├── RideSettings.kt              # Power map, max power, engine braking, regen
│   │   ├── SessionData.kt               # Trip duration, distance, peak speed
│   │   ├── DiagnosticsData.kt           # Fault codes and active warnings
│   │   └── Warning.kt                   # Diagnostic advisories with WarningSeverity
│   ├── repository/TelemetryRepository.kt # Domain interface contract (DIP)
│   └── usecase/GetTelemetryUseCase.kt   # Reusable domain business use case
└── presentation/
    ├── TelemetryUiState.kt              # Sealed interface (Loading, Success, Error)
    ├── TelemetryViewModel.kt            # StateFlow UI management & live simulation
    ├── TelemetryScreen.kt               # Main navigation container (Adaptive portrait/landscape)
    ├── components/                      # Modular Compose components
    │   ├── LoadingContent.kt            # High-tech obsidian skeleton loader with shimmer
    │   ├── ShimmerEffect.kt             # Reusable Modifier.vargShimmer()
    │   ├── ErrorContent.kt              # ECU fault diagnostic state with retry button
    │   ├── BikeShowcase.kt              # Motorcycle hero stage with live sync & charging halo
    │   ├── BatteryCard.kt               # Battery SoC, range, and animated charging beam
    │   ├── PortraitDashboardCard.kt     # Stealth cockpit service tiles
    │   ├── LandscapeCockpitGrid.kt      # Handlebar cockpit layout for landscape mount
    │   └── VargIcons.kt                 # Custom vector Canvas iconography
    └── screens/
        ├── SplashScreen.kt              # Cinematic startup splash screen with CAN-bus init
        ├── BatteryScreen.kt             # High-voltage charging process telemetry & controls
        ├── PowerModesScreen.kt          # Individual M1-M5 mode profile tuning
        ├── MotorScreen.kt               # Powertrain thermal & inverter diagnostic monitor
        ├── StatsScreen.kt               # Session telemetry & circuit tracking
        └── DiagnosticsScreen.kt         # Active advisory log & nominal empty states
```

---

## SOLID Principles

| Principle | Implementation Details |
|---|---|
| **Single Responsibility (SRP)** | Each class has a single reason to change: `TelemetryMapper` only maps; `TelemetryViewModel` manages UI presentation state; `LocalTelemetryDataSource` reads assets; each Compose component renders a distinct telemetry domain. |
| **Open/Closed (OCP)** | `TelemetryDataSource` is an open interface. Connecting to a live BLE / CAN-bus or WebSocket backend requires adding a new `RemoteTelemetryDataSource` without touching the ViewModel, UseCase, or Repository. |
| **Liskov Substitution (LSP)** | `LocalTelemetryDataSource` completely fulfills `TelemetryDataSource`. Any remote or mock implementation can replace it without side effects. |
| **Interface Segregation (ISP)** | `TelemetryRepository` and `TelemetryDataSource` expose focused, minimal contracts (`fetchSnapshot()` / `getTelemetrySnapshot()`). |
| **Dependency Inversion (DIP)** | High-level modules do not depend on low-level modules; both depend on abstractions. `TelemetryViewModel` depends on `GetTelemetryUseCase` and `TelemetryRepository` (interface); concretions are injected via Hilt in `TelemetryModule`. |

---

## State Handling (Assessment Coverage)

### 1. Loading State & Skeletons
* **Obsidian Shimmer (`Modifier.vargShimmer`)**: Smooth animated gradient (`#141418` $\rightarrow$ `#272733` $\rightarrow$ `#141418`) applied across all skeleton elements.
* **Full-Bleed Cockpit Skeletons**: Skeletons mirroring the dashboard layout in both **Portrait** and **Landscape** modes:
  * Brand and model header placeholders.
  * Centered large **Range KM** and **Battery SoC %** metric blocks.
  * Hero motorcycle silhouette pedestal with studio floor reflection.
  * Real-time CAN-Bus sync telemetry banner (`CAN-ID 0x7E0 • 500 KBPS • READING ECU`).
  * 4 service capsule card skeletons.
* **Sync / Refresh Action**: An interactive sync button in the header triggers `viewModel.refreshTelemetry()`, simulating live vehicle CAN-bus network synchronization.

### 2. Error State
* **Diagnostic Fault Screen (`ErrorContent.kt`)**: Displays a dark obsidian card with warning accents, clear human-readable failure descriptions, and a tactile **"RETRY CAN-BUS CONNECTION"** button.

### 3. Empty States
* **Nominal Diagnostics**: When no fault codes or warnings exist, `DiagnosticsScreen` presents a nominal indicator: *"ALL SENSORS NOMINAL — All powertrain sensors and modules operating nominally"*.
* **Advisory Cards**: `WarningsCard` and dashboard alert strips gracefully collapse or reflect zero active advisories when empty.

### 4. Battery Charging Processes
* **Live Powertrain Charging Simulation**: Integrated 3.3 kW AC Stark Stand Charger simulation in `TelemetryViewModel`.
* **Charging Process Card (`BatteryScreen.kt`)**: Real-time grid feed monitoring (`230V • 16A AC`), calculated time-to-target limits, and an interactive **"CONNECT CHARGER & START CHARGING" / "STOP CHARGING / DISCONNECT"** toggle.
* **Animated Feedback**: The motorcycle stage floor switches from red to an electric green halo, and the battery progress bar transitions into an animated charging pulse beam.

---

## Library Choices

| Library | Rationale |
|---|---|
| **Hilt 2.52** | Official Android dependency injection with zero boilerplate; seamless integration with ViewModel and Compose. |
| **AndroidX DataStore Preferences 1.1.2** | Transactional, asynchronous key-value persistence replacing SharedPreferences with Kotlin Flow and coroutine safety. |
| **Moshi 1.15.1 + KSP** | Type-safe JSON serialization without runtime reflection overhead, generating high-performance Kotlin adapters. |
| **Coil Compose 2.7.0** | Asynchronous image loading with native Jetpack Compose integration and caching. |
| **Turbine 1.2.0** | Coroutine `StateFlow` testing library enabling assertions on state transitions without flaky timeouts. |
| **MockK 1.13.13** | Idiomatic Kotlin mocking library with first-class coroutine support (`coEvery`, `coVerify`). |
| **Kotlinx Coroutines Test** | Deterministic virtual-time execution via `StandardTestDispatcher` and `advanceUntilIdle()`. |

---

## Advanced Architecture & Lead Features

### 1. Tactile Haptic Feedback
* Granular physical sensations (`LocalHapticFeedback`) integrated into custom slider interactions (`TextHandleMove`), mode selection pills (`LongPress`), and 3.3 kW charger high-voltage contactor toggle.

### 2. Reactive DataStore Persistence (`UserPreferencesRepository`)
* Non-blocking, reactive local persistence using AndroidX DataStore Preferences.
* Automatically preserves pilot calibrations:
  * Selected power map (`M1`–`M5`).
  * Custom power mode profiles (HP, Engine Braking %, Regen %).
  * Battery target charge threshold (80%, 90%, 100%).
  * Unit system preference (Metric vs Imperial).

### 3. Dynamic Metric & Imperial Localization (`UnitConverter`)
* Dedicated mathematical domain converter between Metric (`km`, `km/h`, `°C`) and Imperial (`mi`, `mph`, `°F`).
* Interactive badge toggle (`[ KM / °C ]` $\leftrightarrow$ `[ MI / °F ]`) in both Portrait Hero showcase and Landscape Handlebar Cockpit.
* 100% test coverage with exhaustive unit test validation.

### 4. 4 Hz Live CAN-Bus Stream Mode (`ObserveLiveTelemetryUseCase`)
* Simulated CAN-bus broadcast stream emitting dynamic throttle curves, motor RPM/power delivery variations, and thermal shifts.
* Pulsing high-tech indicator badge (`[ LIVE 4Hz ]` / `[ STREAM OFF ]`) with seamless Compose recomposition lifecycle handling.

### 5. Universal TalkBack Accessibility
* Complete semantic accessibility (`semantics`, `contentDescription`, `role = Role.Button / Role.RadioButton`, `progressBarRangeInfo`) covering all custom interactive controls, sliders, charging triggers, and service navigation tiles.

---

## Testing Suite

### Unit Tests (`app/src/test/`)
Comprehensive test suite covering positive, negative, and corner cases:
* **`TelemetryMapperTest`**: 14 tests verifying correct mapping of all DTO fields, severity enums, unknown fallbacks, and boundary conditions.
* **`TelemetryRepositoryImplTest`**: 5 tests validating successful domain snapshot emission, `IOException` handling, runtime exceptions, and empty diagnostics mappings.
* **`GetTelemetryUseCaseTest`**: Verifies business logic delegation and error propagation.
* **`UnitConverterTest`**: Comprehensive tests verifying exact rounding, unit conversion formulas (km $\leftrightarrow$ mi, km/h $\leftrightarrow$ mph, °C $\leftrightarrow$ °F), and edge cases.
* **`TelemetryViewModelTest`**: Validates initial `Loading` state, transition to `Success`, error recovery with retry, independent M1–M5 power mode customization, DataStore persistence reactivity, metric/imperial unit toggle, live 4 Hz CAN-bus stream collection, and simulated charging state transitions.

### UI Instrumented Test (`app/src/androidTest/`)
* **`TelemetryScreenTest`**: Android Compose instrumented test verifying the display of bike model info, battery percentage, and active advisory banner.

---

## Build & Run Instructions

```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Assemble the debug APK
./gradlew assembleDebug

# Run instrumented UI tests (requires emulator or device)
./gradlew connectedDebugAndroidTest
```

---

## Trade-offs & Production Readiness

* **Real-time CAN-Bus / BLE Adapter**: `TelemetryDataSource` is designed for seamless drop-in replacement with a real hardware BLE or CAN-FD transceiver socket.
* **Error Granularity**: Sealed `TelemetryUiState.Error` can be expanded into domain-specific error hierarchies (`NetworkError`, `CrcMismatch`, `TimeoutError`) for fine-grained recovery flows.

