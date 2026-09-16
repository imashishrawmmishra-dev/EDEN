package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.calculator.LiveCarbonTrackerEngine
import com.example.calculator.NoiseCalculatorEngine
import com.example.calculator.WaterCalculatorEngine
import com.example.calculator.GhgScenario
import com.example.calculator.GhgScenarioComparison
import com.example.calculator.GhgUnitSystem
import com.example.calculator.GhgUnitConverter
import com.example.util.GhgCsvExporter
import com.example.util.SoundManager
import com.example.data.model.AirPollutantType
import com.example.data.model.AirQualityHourlyPoint
import com.example.data.model.AirQualityStation
import com.example.data.model.EnvironmentalHourlyPoint
import com.example.data.model.NoiseMonitoringStation
import com.example.data.model.NoiseParameterType
import com.example.data.model.ParameterStatus
import com.example.data.model.SoilMonitoringStation
import com.example.data.model.SoilParameterType
import com.example.data.model.WaterMonitoringStation
import com.example.data.model.WaterParameterType
import com.example.data.repository.EnvironmentalMonitoringRepository
import com.example.data.model.AppUpdateInfo
import com.example.data.model.CalculationEntity
import com.example.data.model.CompetencyEntity
import com.example.data.model.DeviceGhgProfile
import com.example.data.model.EdenAnswer
import com.example.data.model.EnvironmentalSensorsState
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity
import com.example.data.model.TopicUserObservation
import com.example.data.model.UserAuthProfile
import com.example.data.remote.EdenAiService
import com.example.data.repository.AirQualityRepository
import com.example.data.repository.EdenRepository
import com.example.location.LiveLocationState
import com.example.location.LocationTracker
import com.example.update.AppUpdateManager
import com.example.monitoring.model.MonitoringProcedureDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class EdenTab(val title: String) {
    HOME("Home"),
    LIVE_CARBON("Live Carbon"),
    ASK_EDEN("Ask EDEN"),
    KNOWLEDGE("Knowledge"),
    CALCULATORS("Calculators"),
    LEARN("Learn"),
    DATA("Data & Sensors"),
    RESOURCES("Resources"),
    PROFILE("Profile"),
    MONITORING_PROCEDURES("Procedures & FDS"),
    ESG_SOLUTIONS("ESG & Solutions")
}

class EdenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EdenRepository(application)
    private val aiService = EdenAiService(repository)
    private val locationTracker = LocationTracker(application)

    private val _currentTab = MutableStateFlow(EdenTab.HOME)
    val currentTab: StateFlow<EdenTab> = _currentTab.asStateFlow()

    // --- Standardized Monitoring Procedures & FDS Section State ---
    private val _selectedProcedureDomain = MutableStateFlow(MonitoringProcedureDomain.AMBIENT)
    val selectedProcedureDomain: StateFlow<MonitoringProcedureDomain> = _selectedProcedureDomain.asStateFlow()

    fun selectProcedureDomain(domain: MonitoringProcedureDomain) {
        _selectedProcedureDomain.value = domain
    }

    fun navigateToProcedures(domain: MonitoringProcedureDomain = MonitoringProcedureDomain.AMBIENT) {
        _selectedProcedureDomain.value = domain
        selectTab(EdenTab.MONITORING_PROCEDURES)
    }

    fun navigateToEsgSolutions() {
        selectTab(EdenTab.ESG_SOLUTIONS)
    }

    // --- User Authentication, Reward Points & Dialogs ---
    private val authPrefs = application.getSharedPreferences("eden_user_session_prefs", Context.MODE_PRIVATE)

    private fun loadInitialAuthProfile(): UserAuthProfile {
        val isLoggedIn = authPrefs.getBoolean("is_logged_in", false)
        return if (isLoggedIn) {
            UserAuthProfile(
                isLoggedIn = true,
                authMethod = authPrefs.getString("auth_method", "SIGN_UP") ?: "SIGN_UP",
                displayName = authPrefs.getString("display_name", "Ashish Mishra") ?: "Ashish Mishra",
                phoneNumber = authPrefs.getString("phone_number", "+1 555-0199") ?: "+1 555-0199",
                countryCode = "+1",
                email = authPrefs.getString("email", "imashishrawmmishra@gmail.com") ?: "imashishrawmmishra@gmail.com",
                purpose = authPrefs.getString("purpose", "Environmental Science & Climate Action") ?: "Environmental Science & Climate Action",
                designation = authPrefs.getString("designation", "Lead Environmental Engineer") ?: "Lead Environmental Engineer",
                rewardPoints = authPrefs.getInt("reward_points", 250),
                organization = authPrefs.getString("organization", "EDEN Environmental Research Group") ?: "EDEN Environmental Research Group",
                institutionType = authPrefs.getString("institution_type", "Ministry") ?: "Ministry",
                institutionName = authPrefs.getString("institution_name", "Ministry of Environment") ?: "Ministry of Environment"
            )
        } else {
            UserAuthProfile(
                isLoggedIn = false,
                authMethod = "GUEST",
                displayName = "Guest Environmentalist",
                rewardPoints = 0
            )
        }
    }

    private val _userAuthProfile = MutableStateFlow(loadInitialAuthProfile())
    val userAuthProfile: StateFlow<UserAuthProfile> = _userAuthProfile.asStateFlow()

    private val _showWelcomeCelebration = MutableStateFlow(false)
    val showWelcomeCelebration: StateFlow<Boolean> = _showWelcomeCelebration.asStateFlow()

    fun dismissWelcomeCelebration() {
        _showWelcomeCelebration.value = false
    }

    // --- Screen Display Mode: Black and White Monochrome vs Full Color ---
    private val _isBlackAndWhiteMode = MutableStateFlow(authPrefs.getBoolean("black_and_white_mode", false))
    val isBlackAndWhiteMode: StateFlow<Boolean> = _isBlackAndWhiteMode.asStateFlow()

    fun setBlackAndWhiteMode(enabled: Boolean) {
        _isBlackAndWhiteMode.value = enabled
        authPrefs.edit().putBoolean("black_and_white_mode", enabled).apply()
    }

    fun toggleBlackAndWhiteMode() {
        setBlackAndWhiteMode(!_isBlackAndWhiteMode.value)
    }

    fun playWelcomeSound() {
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    private val _showSignInDialog = MutableStateFlow(false)
    val showSignInDialog: StateFlow<Boolean> = _showSignInDialog.asStateFlow()

    private val _showAboutDialog = MutableStateFlow(false)
    val showAboutDialog: StateFlow<Boolean> = _showAboutDialog.asStateFlow()

    private val _showJobSearchDialog = MutableStateFlow(false)
    val showJobSearchDialog: StateFlow<Boolean> = _showJobSearchDialog.asStateFlow()

    private val _showDesktopPlatformDialog = MutableStateFlow(false)
    val showDesktopPlatformDialog: StateFlow<Boolean> = _showDesktopPlatformDialog.asStateFlow()

    // --- App Auto-Update System for Customer Devices ---
    private val appUpdateManager = AppUpdateManager(application)
    val appUpdateInfo: StateFlow<AppUpdateInfo> = appUpdateManager.updateInfo

    private val _showUpdateDialog = MutableStateFlow(false)
    val showUpdateDialog: StateFlow<Boolean> = _showUpdateDialog.asStateFlow()

    // --- Live Device GHG Emission state ---
    private val _deviceGhgProfile = MutableStateFlow(
        DeviceGhgProfile(
            deviceType = "Smartphone",
            make = "Android Client",
            model = "Pixel / Modern Smartphone",
            powerWatts = 5.0,
            dailyScreenHours = 6.0,
            gridCarbonIntensityKgKwh = 0.385,
            embodiedCarbonKg = 55.0
        )
    )
    val deviceGhgProfile: StateFlow<DeviceGhgProfile> = _deviceGhgProfile.asStateFlow()

    // --- Live Environmental Sensors State (Indoor/Outdoor & WHO 2021 AQI) ---
    private val _environmentalSensors = MutableStateFlow(EnvironmentalSensorsState())
    val environmentalSensors: StateFlow<EnvironmentalSensorsState> = _environmentalSensors.asStateFlow()

    // --- Live Location Carbon Tracker state ---
    private val _liveLocation = MutableStateFlow(LiveLocationState())
    val liveLocation: StateFlow<LiveLocationState> = _liveLocation.asStateFlow()

    private val _isLiveTracking = MutableStateFlow(false)
    val isLiveTracking: StateFlow<Boolean> = _isLiveTracking.asStateFlow()

    private val _trackedDistanceKm = MutableStateFlow(0.0)
    val trackedDistanceKm: StateFlow<Double> = _trackedDistanceKm.asStateFlow()

    private val _selectedTransitMode = MutableStateFlow(LiveCarbonTrackerEngine.LiveTransitMode.GASOLINE_CAR)
    val selectedTransitMode: StateFlow<LiveCarbonTrackerEngine.LiveTransitMode> = _selectedTransitMode.asStateFlow()

    private val _liveTripCarbon = MutableStateFlow(
        LiveCarbonTrackerEngine.calculateLiveTripEmission(0.0, LiveCarbonTrackerEngine.LiveTransitMode.GASOLINE_CAR)
    )
    val liveTripCarbon: StateFlow<LiveCarbonTrackerEngine.LiveTripCarbonSummary> = _liveTripCarbon.asStateFlow()

    private var trackingJob: Job? = null
    private var lastLat: Double? = null
    private var lastLon: Double? = null

    // --- Ask EDEN state ---
    private val _askQuery = MutableStateFlow("")
    val askQuery: StateFlow<String> = _askQuery.asStateFlow()

    private val _isAsking = MutableStateFlow(false)
    val isAsking: StateFlow<Boolean> = _isAsking.asStateFlow()

    private val _currentAnswer = MutableStateFlow<EdenAnswer?>(null)
    val currentAnswer: StateFlow<EdenAnswer?> = _currentAnswer.asStateFlow()

    private val _forceOffline = MutableStateFlow(false)
    val forceOffline: StateFlow<Boolean> = _forceOffline.asStateFlow()

    // --- Knowledge Graph state ---
    private val _knowledgeSearchQuery = MutableStateFlow("")
    val knowledgeSearchQuery: StateFlow<String> = _knowledgeSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedNode = MutableStateFlow<KnowledgeEntity?>(null)
    val selectedNode: StateFlow<KnowledgeEntity?> = _selectedNode.asStateFlow()

    private val _detailLevel = MutableStateFlow(1) // 1..5
    val detailLevel: StateFlow<Int> = _detailLevel.asStateFlow()

    val knowledgeNodes: StateFlow<List<KnowledgeEntity>> = repository.getAllKnowledge()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val resources: StateFlow<List<ResourceEntity>> = repository.getAllResources()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calculationHistory: StateFlow<List<CalculationEntity>> = repository.getRecentCalculations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monitoringPoints: StateFlow<List<MonitoringEntity>> = repository.getAllMonitoringPoints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val competencies: StateFlow<List<CompetencyEntity>> = repository.getAllCompetencies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Calculators state ---
    private val _carbonResult = MutableStateFlow<CarbonCalculatorEngine.CarbonInventoryResult?>(null)
    val carbonResult: StateFlow<CarbonCalculatorEngine.CarbonInventoryResult?> = _carbonResult.asStateFlow()

    private val _lastCarbonInputs = MutableStateFlow(GhgCsvExporter.GhgInputParameters())
    val lastCarbonInputs: StateFlow<GhgCsvExporter.GhgInputParameters> = _lastCarbonInputs.asStateFlow()

    private val _showGhgCsvExportModal = MutableStateFlow(false)
    val showGhgCsvExportModal: StateFlow<Boolean> = _showGhgCsvExportModal.asStateFlow()

    fun setGhgCsvExportModal(show: Boolean) {
        _showGhgCsvExportModal.value = show
    }

    // --- Global GHG Unit Switcher State (Metric vs Imperial) ---
    private val _ghgUnitSystem = MutableStateFlow(GhgUnitSystem.METRIC)
    val ghgUnitSystem: StateFlow<GhgUnitSystem> = _ghgUnitSystem.asStateFlow()

    fun setGhgUnitSystem(system: GhgUnitSystem) {
        _ghgUnitSystem.value = system
    }

    // --- GHG Scenarios & Side-by-Side Comparative Analysis ---
    private val _savedGhgScenarios = MutableStateFlow<List<GhgScenario>>(emptyList())
    val savedGhgScenarios: StateFlow<List<GhgScenario>> = _savedGhgScenarios.asStateFlow()

    private val _selectedBaselineScenarioId = MutableStateFlow<String?>(null)
    val selectedBaselineScenarioId: StateFlow<String?> = _selectedBaselineScenarioId.asStateFlow()

    private val _isComparisonModeActive = MutableStateFlow(false)
    val isComparisonModeActive: StateFlow<Boolean> = _isComparisonModeActive.asStateFlow()

    private val _showSaveScenarioDialog = MutableStateFlow(false)
    val showSaveScenarioDialog: StateFlow<Boolean> = _showSaveScenarioDialog.asStateFlow()

    fun setShowSaveScenarioDialog(show: Boolean) {
        _showSaveScenarioDialog.value = show
    }

    fun toggleComparisonMode(active: Boolean? = null) {
        _isComparisonModeActive.value = active ?: !_isComparisonModeActive.value
    }

    fun setBaselineScenario(id: String) {
        _selectedBaselineScenarioId.value = id
        _savedGhgScenarios.value = _savedGhgScenarios.value.map {
            it.copy(isBaseline = it.id == id)
        }
    }

    fun saveCurrentCalculationAsScenario(name: String, description: String = "", setAsBaseline: Boolean = false): GhgScenario {
        val currentInputs = _lastCarbonInputs.value
        val currentResult = _carbonResult.value ?: CarbonCalculatorEngine.calculateInventory(
            currentInputs.fuelType,
            currentInputs.fuelQuantity,
            currentInputs.electricityKwh,
            currentInputs.gridRegion,
            currentInputs.transportMode,
            currentInputs.transportVolume
        )

        val newScenario = GhgScenario(
            id = java.util.UUID.randomUUID().toString(),
            name = name.ifBlank { "Scenario ${java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.US).format(java.util.Date())}" },
            description = description,
            timestamp = System.currentTimeMillis(),
            inputs = currentInputs,
            result = currentResult,
            isBaseline = setAsBaseline || _savedGhgScenarios.value.isEmpty()
        )

        val updatedList = if (newScenario.isBaseline) {
            _savedGhgScenarios.value.map { it.copy(isBaseline = false) } + newScenario
        } else {
            _savedGhgScenarios.value + newScenario
        }
        _savedGhgScenarios.value = updatedList
        if (newScenario.isBaseline) {
            _selectedBaselineScenarioId.value = newScenario.id
        }

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "GHG Scenario Snapshot",
                    inputDescription = "Scenario '${newScenario.name}': ${newScenario.inputs.fuelType.displayName} ${newScenario.inputs.fuelQuantity} ${newScenario.inputs.fuelType.unit}, ${newScenario.inputs.electricityKwh} kWh",
                    resultSummary = "Total: %.2f t CO₂e (Baseline: ${newScenario.isBaseline})".format(newScenario.result.totalTonnes),
                    units = "tonnes CO₂e",
                    assumptions = "Saved corporate scenario for comparative GHG benchmarking."
                )
            )
        }

        return newScenario
    }

    fun deleteScenario(id: String) {
        val current = _savedGhgScenarios.value
        val updated = current.filterNot { it.id == id }
        _savedGhgScenarios.value = updated
        if (_selectedBaselineScenarioId.value == id) {
            val nextBaseline = updated.firstOrNull()
            _selectedBaselineScenarioId.value = nextBaseline?.id
            _savedGhgScenarios.value = updated.mapIndexed { idx, item ->
                if (idx == 0) item.copy(isBaseline = true) else item
            }
        }
    }

    fun getComparisonResult(): GhgScenarioComparison? {
        val baseline = _savedGhgScenarios.value.find { it.id == _selectedBaselineScenarioId.value }
            ?: _savedGhgScenarios.value.find { it.isBaseline }
            ?: _savedGhgScenarios.value.firstOrNull()
            ?: return null

        val currentResult = _carbonResult.value ?: CarbonCalculatorEngine.calculateInventory(
            _lastCarbonInputs.value.fuelType,
            _lastCarbonInputs.value.fuelQuantity,
            _lastCarbonInputs.value.electricityKwh,
            _lastCarbonInputs.value.gridRegion,
            _lastCarbonInputs.value.transportMode,
            _lastCarbonInputs.value.transportVolume
        )

        val currentScenario = GhgScenario(
            id = "active_current_calc",
            name = "Current Active Calculation",
            timestamp = System.currentTimeMillis(),
            inputs = _lastCarbonInputs.value,
            result = currentResult
        )

        return GhgScenarioComparison(baseline = baseline, current = currentScenario)
    }

    private val _airResult = MutableStateFlow<AirCalculatorEngine.StackFlowResult?>(null)
    val airResult: StateFlow<AirCalculatorEngine.StackFlowResult?> = _airResult.asStateFlow()

    private val _waterResult = MutableStateFlow<WaterCalculatorEngine.WaterLoadResult?>(null)
    val waterResult: StateFlow<WaterCalculatorEngine.WaterLoadResult?> = _waterResult.asStateFlow()

    private val _noiseResult = MutableStateFlow<NoiseCalculatorEngine.AcousticResult?>(null)
    val noiseResult: StateFlow<NoiseCalculatorEngine.AcousticResult?> = _noiseResult.asStateFlow()

    // --- User Profile & Organization ---
    private val _userPersona = MutableStateFlow("Environmental Consultant")
    val userPersona: StateFlow<String> = _userPersona.asStateFlow()

    private val _orgName = MutableStateFlow("EDEN — Environmental Intelligence & Learning")
    val orgName: StateFlow<String> = _orgName.asStateFlow()

    private val _orgWebsite = MutableStateFlow("https://eden-environmental-intelligence.onrender.com")
    val orgWebsite: StateFlow<String> = _orgWebsite.asStateFlow()

    init {
        // Initialize default enterprise baseline scenario for instant comparison
        val defaultBaselineInputs = GhgCsvExporter.GhgInputParameters(
            fuelType = CarbonCalculatorEngine.FuelType.DIESEL,
            fuelQuantity = 2000.0,
            electricityKwh = 25000.0,
            gridRegion = CarbonCalculatorEngine.GridRegion.US_AVERAGE,
            transportMode = CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            transportVolume = 8000.0,
            timestamp = System.currentTimeMillis() - 86400000L * 14
        )
        val defaultBaselineResult = CarbonCalculatorEngine.calculateInventory(
            defaultBaselineInputs.fuelType,
            defaultBaselineInputs.fuelQuantity,
            defaultBaselineInputs.electricityKwh,
            defaultBaselineInputs.gridRegion,
            defaultBaselineInputs.transportMode,
            defaultBaselineInputs.transportVolume
        )
        val initialBaseline = GhgScenario(
            id = "scenario_baseline_fy24",
            name = "FY24 Facility Baseline (Diesel + US Grid)",
            description = "Standard operational baseline prior to efficiency interventions",
            timestamp = System.currentTimeMillis() - 86400000L * 14,
            inputs = defaultBaselineInputs,
            result = defaultBaselineResult,
            isBaseline = true
        )
        _savedGhgScenarios.value = listOf(initialBaseline)
        _selectedBaselineScenarioId.value = initialBaseline.id

        viewModelScope.launch {
            repository.ensureSeeded()
            // Set initial sample query answer for immediate exploration
            askEdenQuestion("What is the relationship between BOD and Dissolved Oxygen?")
        }
    }

    fun selectTab(tab: EdenTab) {
        _currentTab.value = tab
    }

    fun setAskQuery(q: String) {
        _askQuery.value = q
    }

    fun toggleForceOffline(enabled: Boolean) {
        _forceOffline.value = enabled
    }

    fun askEdenQuestion(q: String) {
        val query = q.ifBlank { _askQuery.value }
        if (query.isBlank()) return
        _askQuery.value = query
        _isAsking.value = true

        viewModelScope.launch {
            try {
                val answer = aiService.answerQuestion(query, _forceOffline.value)
                _currentAnswer.value = answer
            } catch (e: Exception) {
                // Should not happen as aiService wraps with fallback
            } finally {
                _isAsking.value = false
            }
        }
    }

    fun setKnowledgeSearch(query: String) {
        _knowledgeSearchQuery.value = query
    }

    fun setCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun selectKnowledgeNode(node: KnowledgeEntity?) {
        _selectedNode.value = node
        _detailLevel.value = 1
    }

    fun setDetailLevel(level: Int) {
        _detailLevel.value = level.coerceIn(1, 5)
    }

    fun setUserPersona(persona: String) {
        _userPersona.value = persona
    }

    fun updateOrganization(name: String, website: String) {
        if (name.isNotBlank()) _orgName.value = name.trim()
        if (website.isNotBlank()) {
            var url = website.trim()
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            _orgWebsite.value = url
        }
    }

    // --- Authentication & Reward Points Methods ---
    fun setSignInDialog(show: Boolean) {
        _showSignInDialog.value = show
    }

    fun setAboutDialog(show: Boolean) {
        _showAboutDialog.value = show
    }

    fun setJobSearchDialog(show: Boolean) {
        _showJobSearchDialog.value = show
    }

    fun setDesktopPlatformDialog(show: Boolean) {
        _showDesktopPlatformDialog.value = show
    }

    private fun persistAuthProfile(profile: UserAuthProfile) {
        authPrefs.edit()
            .putBoolean("is_logged_in", profile.isLoggedIn)
            .putString("display_name", profile.displayName)
            .putString("designation", profile.designation)
            .putString("institution_type", profile.institutionType)
            .putString("institution_name", profile.institutionName)
            .putString("email", profile.email)
            .putString("phone_number", profile.phoneNumber)
            .putString("auth_method", profile.authMethod)
            .putString("organization", profile.organization)
            .putString("purpose", profile.purpose)
            .putInt("reward_points", profile.rewardPoints)
            .apply()
    }

    fun registerAndVerifyUser(
        name: String,
        designation: String,
        institutionType: String,
        institutionName: String,
        contact: String
    ) {
        val isPhone = contact.contains(Regex("^\\+?[0-9\\-\\s]+$"))
        val welcomeBonusPoints = 250 // Initial welcome bonus on sign-up & OTP verification
        val newProfile = UserAuthProfile(
            isLoggedIn = true,
            authMethod = "SIGN_UP",
            displayName = name.trim().ifBlank { "Environmental Pioneer" },
            designation = designation.trim().ifBlank { "Sustainability Professional" },
            institutionType = institutionType.ifBlank { "Office" },
            institutionName = institutionName.trim().ifBlank { "EDEN Partner Network" },
            organization = institutionName.trim().ifBlank { "EDEN Community" },
            phoneNumber = if (isPhone) contact.trim() else "",
            email = if (!isPhone) contact.trim() else "",
            rewardPoints = welcomeBonusPoints,
            purpose = "Planetary Health & Environmental Intelligence"
        )
        _userAuthProfile.value = newProfile
        persistAuthProfile(newProfile)
        _showSignInDialog.value = false
        _showWelcomeCelebration.value = true
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    fun signInExistingUser(
        contact: String,
        name: String = "",
        designation: String = "",
        institutionType: String = "",
        institutionName: String = ""
    ) {
        val current = _userAuthProfile.value
        val isPhone = contact.contains(Regex("^\\+?[0-9\\-\\s]+$"))
        val points = if (current.rewardPoints > 0) current.rewardPoints else 250
        val updated = current.copy(
            isLoggedIn = true,
            authMethod = "SIGN_IN",
            displayName = name.trim().ifBlank {
                if (current.displayName.isNotBlank() && current.displayName != "Guest Environmentalist") current.displayName else "Ashish Mishra"
            },
            designation = designation.trim().ifBlank { current.designation },
            institutionType = institutionType.ifBlank { current.institutionType },
            institutionName = institutionName.trim().ifBlank { current.institutionName },
            phoneNumber = if (isPhone) contact.trim() else current.phoneNumber,
            email = if (!isPhone) contact.trim() else current.email,
            rewardPoints = points
        )
        _userAuthProfile.value = updated
        persistAuthProfile(updated)
        _showSignInDialog.value = false
        _showWelcomeCelebration.value = true
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    fun signInWithPhone(countryCode: String, number: String, name: String) {
        val current = _userAuthProfile.value
        val updated = current.copy(
            isLoggedIn = true,
            authMethod = "PHONE",
            countryCode = countryCode.ifBlank { "+1" },
            phoneNumber = number,
            displayName = name.ifBlank { "Eco Champion" },
            rewardPoints = (current.rewardPoints + 100).coerceAtLeast(250)
        )
        _userAuthProfile.value = updated
        persistAuthProfile(updated)
        _showSignInDialog.value = false
        _showWelcomeCelebration.value = true
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    fun signInWithGoogle(accountName: String, email: String) {
        val current = _userAuthProfile.value
        val updated = current.copy(
            isLoggedIn = true,
            authMethod = "GOOGLE",
            displayName = accountName.ifBlank { "Google Verified Scholar" },
            email = email.ifBlank { "user@gmail.com" },
            rewardPoints = (current.rewardPoints + 100).coerceAtLeast(250)
        )
        _userAuthProfile.value = updated
        persistAuthProfile(updated)
        _showSignInDialog.value = false
        _showWelcomeCelebration.value = true
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    fun signInFreeAccess(purpose: String, designation: String, name: String) {
        val current = _userAuthProfile.value
        val updated = current.copy(
            isLoggedIn = true,
            authMethod = "PURPOSE_DESIGNATION",
            displayName = name.ifBlank { "Sustainability Researcher" },
            purpose = purpose.ifBlank { "Environmental Research & Climate Action" },
            designation = designation.ifBlank { "Environmental Specialist" },
            rewardPoints = (current.rewardPoints + 100).coerceAtLeast(250)
        )
        _userAuthProfile.value = updated
        persistAuthProfile(updated)
        _showSignInDialog.value = false
        _showWelcomeCelebration.value = true
        SoundManager.playWelcomeSound(getApplication<Application>().applicationContext)
    }

    fun signOut() {
        authPrefs.edit().clear().apply()
        _userAuthProfile.value = UserAuthProfile(
            isLoggedIn = false,
            authMethod = "GUEST",
            displayName = "Guest Environmentalist",
            rewardPoints = 0
        )
        _showWelcomeCelebration.value = false
    }

    fun awardEcoPoints(points: Int) {
        val current = _userAuthProfile.value
        val updated = current.copy(rewardPoints = current.rewardPoints + points)
        _userAuthProfile.value = updated
        persistAuthProfile(updated)
    }

    fun updateDeviceProfile(
        deviceType: String,
        make: String,
        model: String,
        powerWatts: Double,
        dailyHours: Double
    ) {
        val embodied = when (deviceType.lowercase()) {
            "laptop" -> 220.0
            "desktop pc", "workstation/server" -> 420.0
            "tablet" -> 85.0
            else -> 55.0
        }
        _deviceGhgProfile.value = DeviceGhgProfile(
            deviceType = deviceType,
            make = make,
            model = model,
            powerWatts = powerWatts.coerceAtLeast(0.5),
            dailyScreenHours = dailyHours.coerceIn(0.5, 24.0),
            embodiedCarbonKg = embodied
        )
    }

    fun refreshEnvironmentalSensors(
        inTemp: Double = 22.4,
        outTemp: Double = 28.6,
        inHum: Double = 47.5,
        outHum: Double = 58.0,
        pm25: Double = 11.4,
        pm10: Double = 28.2
    ) {
        _environmentalSensors.value = _environmentalSensors.value.copy(
            indoorTempC = inTemp,
            outdoorTempC = outTemp,
            indoorHumidityRh = inHum,
            outdoorHumidityRh = outHum,
            pm25 = pm25,
            pm10 = pm10
        )
    }

    // --- Calculator Actions ---
    fun calculateCarbon(
        fuel: CarbonCalculatorEngine.FuelType,
        qty: Double,
        kwh: Double,
        grid: CarbonCalculatorEngine.GridRegion,
        mode: CarbonCalculatorEngine.TransportMode,
        travelVol: Double
    ) {
        val result = CarbonCalculatorEngine.calculateInventory(fuel, qty, kwh, grid, mode, travelVol)
        _carbonResult.value = result
        _lastCarbonInputs.value = GhgCsvExporter.GhgInputParameters(
            fuelType = fuel,
            fuelQuantity = qty,
            electricityKwh = kwh,
            gridRegion = grid,
            transportMode = mode,
            transportVolume = travelVol,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "Carbon GHG Scope 1/2/3",
                    inputDescription = "${fuel.displayName}: $qty ${fuel.unit}, Electricity: $kwh kWh, Freight/Travel: $travelVol ${mode.unit}",
                    resultSummary = "Total: %.2f t CO₂e (S1: %.1f%%, S2: %.1f%%, S3: %.1f%%)".format(
                        result.totalTonnes, result.scope1Percent, result.scope2Percent, result.scope3Percent
                    ),
                    units = "tonnes CO₂e",
                    assumptions = result.methodologyNote
                )
            )
        }
    }

    fun calculateAirStack(velocity: Double, diameter: Double, concMgM3: Double, tempCelsius: Double = 165.0) {
        val result = AirCalculatorEngine.calculateStackFlow(velocity, diameter, concMgM3, tempCelsius)
        _airResult.value = result

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "Air Stack Emission Rate",
                    inputDescription = "Velocity: $velocity m/s, Diameter: $diameter m, Temp: $tempCelsius °C, Conc: $concMgM3 mg/Nm³",
                    resultSummary = "Flow: %.1f m³/h (%.1f Nm³/h), Emission Rate: %.4f kg/h (%.4f g/s)".format(
                        result.flowRateM3Hour, result.normalizedFlowRateM3Hour, result.emissionRateKgHour, result.emissionRateGSec
                    ),
                    units = "kg/h",
                    assumptions = result.formulaExplanation
                )
            )
        }
    }

    fun calculateWater(flowM3Day: Double, bodMgL: Double, codMgL: Double, tankVolM3: Double) {
        val result = WaterCalculatorEngine.calculateWastewaterMetrics(flowM3Day, bodMgL, codMgL, tankVolM3)
        _waterResult.value = result

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "Wastewater BOD/COD Loading",
                    inputDescription = "Flow: $flowM3Day m³/day, BOD: $bodMgL mg/L, COD: $codMgL mg/L",
                    resultSummary = "BOD Load: %.1f kg/d, PE: %.0f cap, HRT: %.1fh, Class: %s".format(
                        result.bodLoadKgDay, result.populationEquivalent, result.hrtHours, result.biodegradabilityClass
                    ),
                    units = "kg/day & PE",
                    assumptions = "1 PE = 0.06 kg BOD5/day. " + result.treatmentRecommendation
                )
            )
        }
    }

    fun calculateNoise(sources: List<Double>, dayLeq: Double, nightLeq: Double, refDist: Double, targetDist: Double) {
        val result = NoiseCalculatorEngine.calculateAcoustics(sources, dayLeq, nightLeq, refDist, targetDist)
        _noiseResult.value = result

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "Environmental Noise & Ldn",
                    inputDescription = "Sources: ${sources.joinToString()} dBA, Day: $dayLeq, Night: $nightLeq, Dist: $refDist->$targetDist m",
                    resultSummary = "L_total: %.1f dBA, L_dn: %.1f dBA, Attenuated: %.1f dBA".format(
                        result.totalDecibels, result.dayNightLevelLdn, result.attenuatedDbAtDistance
                    ),
                    units = "dBA",
                    assumptions = result.complianceStatus
                )
            )
        }
    }

    fun addFieldMonitoringPoint(
        param: String,
        value: Double,
        unit: String,
        location: String,
        standardLimit: Double,
        standardSource: String
    ) {
        viewModelScope.launch {
            repository.addMonitoringPoint(
                MonitoringEntity(
                    parameter = param,
                    value = value,
                    unit = unit,
                    location = location,
                    standardLimit = standardLimit,
                    standardSource = standardSource
                )
            )
        }
    }

    fun updateCompetency(id: String, progress: Int) {
        viewModelScope.launch {
            repository.updateCompetencyProgress(id, progress, progress >= 100)
        }
    }

    // --- Live Location Carbon Tracker Actions ---
    fun startLiveTracking() {
        if (_isLiveTracking.value) return
        _isLiveTracking.value = true

        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            locationTracker.getLocationFlow(intervalMs = 2500L).collect { loc ->
                _liveLocation.value = loc
                if (loc.hasFix) {
                    val prevLat = lastLat
                    val prevLon = lastLon

                    if (prevLat != null && prevLon != null) {
                        val distanceDelta = LiveCarbonTrackerEngine.haversineDistanceKm(
                            prevLat, prevLon, loc.latitude, loc.longitude
                        )
                        // Filter out GPS drift jitter (< 3 meters = 0.003 km) unless high speed
                        if (distanceDelta >= 0.003 || loc.speedKmh > 5.0) {
                            val newDist = _trackedDistanceKm.value + distanceDelta
                            _trackedDistanceKm.value = newDist
                            _liveTripCarbon.value = LiveCarbonTrackerEngine.calculateLiveTripEmission(
                                newDist, _selectedTransitMode.value
                            )
                        }
                    }

                    lastLat = loc.latitude
                    lastLon = loc.longitude
                }
            }
        }
    }

    fun stopLiveTracking() {
        _isLiveTracking.value = false
        trackingJob?.cancel()
        trackingJob = null

        val distance = _trackedDistanceKm.value
        val mode = _selectedTransitMode.value
        val trip = _liveTripCarbon.value

        if (distance > 0.01) {
            viewModelScope.launch {
                repository.saveCalculation(
                    CalculationEntity(
                        calculatorType = "Live Location Trip Carbon",
                        inputDescription = "Mode: ${mode.displayName}, Distance: %.2f km, Lat/Lon: %.4f, %.4f".format(
                            distance, _liveLocation.value.latitude, _liveLocation.value.longitude
                        ),
                        resultSummary = "Emitted: %.3f kg CO₂e (Saved: %.3f kg CO₂e vs car)".format(
                            trip.carbonEmittedKg, trip.carbonSavedVsCarKg
                        ),
                        units = "kg CO₂e",
                        assumptions = "DEFRA 2023 / EPA GHG emission factors. Real-time GPS distance accumulation."
                    )
                )
            }
        }
    }

    fun selectTransitMode(mode: LiveCarbonTrackerEngine.LiveTransitMode) {
        _selectedTransitMode.value = mode
        _liveTripCarbon.value = LiveCarbonTrackerEngine.calculateLiveTripEmission(
            _trackedDistanceKm.value, mode
        )
    }

    fun resetLiveTrip() {
        stopLiveTracking()
        _trackedDistanceKm.value = 0.0
        lastLat = null
        lastLon = null
        _liveTripCarbon.value = LiveCarbonTrackerEngine.calculateLiveTripEmission(
            0.0, _selectedTransitMode.value
        )
    }

    fun clearCalcHistory() {
        viewModelScope.launch {
            repository.clearCalculations()
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            repository.deleteCalculation(id)
        }
    }

    // --- Auto-Update Management Actions ---
    fun setUpdateDialog(show: Boolean) {
        _showUpdateDialog.value = show
    }

    fun checkForAppUpdates(manual: Boolean = false) {
        appUpdateManager.checkForUpdates(manual)
    }

    fun startDownloadAndInstall(context: android.content.Context, openStore: Boolean = false) {
        appUpdateManager.startDownloadAndInstall(context, openStore)
    }

    fun setAutoCheckUpdates(enabled: Boolean) {
        appUpdateManager.setAutoCheck(enabled)
    }

    fun setAutoDownloadWifi(enabled: Boolean) {
        appUpdateManager.setAutoDownloadWifi(enabled)
    }

    fun dismissUpdate() {
        appUpdateManager.dismissUpdate()
        _showUpdateDialog.value = false
    }

    fun triggerTestUpdate() {
        appUpdateManager.triggerTestUpdateAvailable()
        _showUpdateDialog.value = true
    }

    // --- Environmental Intelligence Field Observations & Research Inputs ---
    private val _topicObservations = MutableStateFlow<List<TopicUserObservation>>(
        listOf(
            TopicUserObservation(
                topicId = "air_quality",
                parameterValue = "11.8 µg/m³",
                location = "Urban Canopy Station Alpha",
                observationNotes = "Baseline laser photometer check. Micro-particles well within WHO 2021 daily guidance (≤15 µg/m³).",
                complianceStatus = "Compliant"
            ),
            TopicUserObservation(
                topicId = "water_quality",
                parameterValue = "7.6 mg/L",
                location = "Riparian Catchment Basin #4",
                observationNotes = "Electrochemical DO probe reading. Healthy dissolved oxygen supporting benthic macroinvertebrates.",
                complianceStatus = "Compliant"
            ),
            TopicUserObservation(
                topicId = "noise_quality",
                parameterValue = "52.4 dBA",
                location = "Residential Green Corridor",
                observationNotes = "Type 1 sound level meter 15-minute Leq. Meets WHO residential daytime recommendation (≤53 dBA).",
                complianceStatus = "Compliant"
            )
        )
    )
    val topicObservations: StateFlow<List<TopicUserObservation>> = _topicObservations.asStateFlow()

    fun addTopicObservation(
        topicId: String,
        parameterValue: String,
        location: String,
        notes: String,
        complianceStatus: String
    ) {
        val newObservation = TopicUserObservation(
            topicId = topicId,
            parameterValue = parameterValue,
            location = location.ifBlank { "Field Station" },
            observationNotes = notes.ifBlank { "Standard field measurement logged." },
            complianceStatus = complianceStatus
        )
        _topicObservations.value = listOf(newObservation) + _topicObservations.value
        // Award EcoPoints for active research & monitoring field logging
        awardEcoPoints(10)
    }

    fun deleteTopicObservation(id: String) {
        _topicObservations.value = _topicObservations.value.filterNot { it.id == id }
    }

    // --- Dedicated Air Quality Monitoring Section State ---
    private val _airStations = MutableStateFlow<List<AirQualityStation>>(AirQualityRepository.defaultStations)
    val airStations: StateFlow<List<AirQualityStation>> = _airStations.asStateFlow()

    private val _selectedAirStationId = MutableStateFlow(AirQualityRepository.defaultStations.first().id)
    val selectedAirStationId: StateFlow<String> = _selectedAirStationId.asStateFlow()

    private val _currentAirQualityStation = MutableStateFlow(AirQualityRepository.defaultStations.first())
    val currentAirQualityStation: StateFlow<AirQualityStation> = _currentAirQualityStation.asStateFlow()

    private val _isAirQualityThresholdExceeded = MutableStateFlow(AirQualityRepository.defaultStations.first().hasThresholdAlert)
    val isAirQualityThresholdExceeded: StateFlow<Boolean> = _isAirQualityThresholdExceeded.asStateFlow()

    private fun syncAirQualityAlertState() {
        val current = _airStations.value.find { it.id == _selectedAirStationId.value } ?: _airStations.value.first()
        _currentAirQualityStation.value = current
        _isAirQualityThresholdExceeded.value = current.hasThresholdAlert
    }

    private val _selectedAirPollutant = MutableStateFlow(AirPollutantType.PM25)
    val selectedAirPollutant: StateFlow<AirPollutantType> = _selectedAirPollutant.asStateFlow()

    private val _selectedHourlyPointIndex = MutableStateFlow<Int?>(null)
    val selectedHourlyPointIndex: StateFlow<Int?> = _selectedHourlyPointIndex.asStateFlow()

    fun selectAirPollutant(pollutant: AirPollutantType) {
        _selectedAirPollutant.value = pollutant
        _selectedHourlyPointIndex.value = null
    }

    fun selectAirStation(stationId: String) {
        _selectedAirStationId.value = stationId
        _selectedHourlyPointIndex.value = null
        _dismissedAlertStation.value = null
        syncAirQualityAlertState()
    }

    private val _airQualityAlertsEnabled = MutableStateFlow(true)
    val airQualityAlertsEnabled: StateFlow<Boolean> = _airQualityAlertsEnabled.asStateFlow()

    private val _dismissedAlertStation = MutableStateFlow<String?>(null)
    val dismissedAlertStation: StateFlow<String?> = _dismissedAlertStation.asStateFlow()

    fun toggleAirQualityAlerts(enabled: Boolean) {
        _airQualityAlertsEnabled.value = enabled
    }

    fun dismissAirQualityAlert(stationId: String) {
        _dismissedAlertStation.value = stationId
    }

    fun resetDismissedAlert() {
        _dismissedAlertStation.value = null
    }

    fun selectHourlyPoint(index: Int?) {
        _selectedHourlyPointIndex.value = index
    }

    fun refreshAirQualityReadings() {
        _dismissedAlertStation.value = null
        // Slightly fluctuate current station readings to simulate live telemetry refresh
        val currentStations = _airStations.value
        val updated = currentStations.map { station ->
            val delta = (Math.random() * 1.6) - 0.8
            val newPm25 = (station.pm25 + delta).coerceIn(4.0, 45.0)
            val newPm10 = (station.pm10 + delta * 2.1).coerceIn(10.0, 90.0)
            val newNo2 = (station.no2 + delta * 1.2).coerceIn(5.0, 50.0)
            val newO3 = (station.o3 + delta * 2.5).coerceIn(20.0, 110.0)
            station.copy(
                pm25 = Math.round(newPm25 * 10.0) / 10.0,
                pm10 = Math.round(newPm10 * 10.0) / 10.0,
                no2 = Math.round(newNo2 * 10.0) / 10.0,
                o3 = Math.round(newO3 * 10.0) / 10.0,
                lastUpdated = "Updated just now (Telemetry Sync)"
            )
        }
        _airStations.value = updated
        // Also update environmental sensors state so the entire dashboard stays in sync
        val currentStation = updated.find { it.id == _selectedAirStationId.value } ?: updated.first()
        _environmentalSensors.value = _environmentalSensors.value.copy(
            pm25 = currentStation.pm25,
            pm10 = currentStation.pm10,
            no2 = currentStation.no2,
            o3 = currentStation.o3
        )
        syncAirQualityAlertState()
    }

    fun simulateAirSpike(pollutant: AirPollutantType) {
        _dismissedAlertStation.value = null
        val currentStations = _airStations.value
        val updated = currentStations.map { station ->
            if (station.id == _selectedAirStationId.value) {
                when (pollutant) {
                    AirPollutantType.PM25 -> station.copy(pm25 = 38.4, lastUpdated = "Alert: Transient Peak Logged")
                    AirPollutantType.PM10 -> station.copy(pm10 = 74.0, lastUpdated = "Alert: Dust Inversion Event")
                    AirPollutantType.NO2 -> station.copy(no2 = 36.2, lastUpdated = "Alert: Rush Hour Traffic Inversion")
                    AirPollutantType.O3 -> station.copy(o3 = 108.5, lastUpdated = "Alert: Solar Smog Photochemical Peak")
                }
            } else station
        }
        _airStations.value = updated
        val active = updated.find { it.id == _selectedAirStationId.value } ?: updated.first()
        _environmentalSensors.value = _environmentalSensors.value.copy(
            pm25 = active.pm25,
            pm10 = active.pm10,
            no2 = active.no2,
            o3 = active.o3
        )
        syncAirQualityAlertState()
    }

    private val _showAirQualityAlertDialog = MutableStateFlow(false)
    val showAirQualityAlertDialog: StateFlow<Boolean> = _showAirQualityAlertDialog.asStateFlow()

    fun setAirQualityAlertDialog(show: Boolean) {
        _showAirQualityAlertDialog.value = show
    }

    // =========================================================================
    // MULTI-DOMAIN ENVIRONMENTAL MONITORING SUITE (WATER, NOISE, SOIL)
    // =========================================================================

    enum class MonitoringDomainTab(val label: String, val badge: String) {
        AIR("Air Quality", "PM2.5/NO₂"),
        WATER("Water Quality", "DO/BOD/COD"),
        NOISE("Noise & Acoustics", "dB(A) Leq"),
        SOIL("Soil & Ecology", "pH/SOM/Pb")
    }

    private val _selectedMonitoringDomainTab = MutableStateFlow(MonitoringDomainTab.AIR)
    val selectedMonitoringDomainTab: StateFlow<MonitoringDomainTab> = _selectedMonitoringDomainTab.asStateFlow()

    fun selectMonitoringDomainTab(tab: MonitoringDomainTab) {
        _selectedMonitoringDomainTab.value = tab
    }

    // --- Water Quality Monitoring State ---
    private val _waterStations = MutableStateFlow<List<WaterMonitoringStation>>(EnvironmentalMonitoringRepository.defaultWaterStations)
    val waterStations: StateFlow<List<WaterMonitoringStation>> = _waterStations.asStateFlow()

    private val _selectedWaterStationId = MutableStateFlow(EnvironmentalMonitoringRepository.defaultWaterStations.first().id)
    val selectedWaterStationId: StateFlow<String> = _selectedWaterStationId.asStateFlow()

    private val _selectedWaterParameter = MutableStateFlow(WaterParameterType.DISSOLVED_OXYGEN)
    val selectedWaterParameter: StateFlow<WaterParameterType> = _selectedWaterParameter.asStateFlow()

    private val _selectedWaterHourlyIndex = MutableStateFlow<Int?>(null)
    val selectedWaterHourlyIndex: StateFlow<Int?> = _selectedWaterHourlyIndex.asStateFlow()

    private val _waterAlertsEnabled = MutableStateFlow(true)
    val waterAlertsEnabled: StateFlow<Boolean> = _waterAlertsEnabled.asStateFlow()

    private val _dismissedWaterStation = MutableStateFlow<String?>(null)
    val dismissedWaterStation: StateFlow<String?> = _dismissedWaterStation.asStateFlow()

    fun selectWaterStation(id: String) {
        _selectedWaterStationId.value = id
        _selectedWaterHourlyIndex.value = null
        _dismissedWaterStation.value = null
    }

    fun selectWaterParameter(param: WaterParameterType) {
        _selectedWaterParameter.value = param
        _selectedWaterHourlyIndex.value = null
    }

    fun selectWaterHourlyPoint(index: Int?) {
        _selectedWaterHourlyIndex.value = index
    }

    fun toggleWaterAlerts(enabled: Boolean) {
        _waterAlertsEnabled.value = enabled
    }

    fun dismissWaterAlert(stationId: String) {
        _dismissedWaterStation.value = stationId
    }

    fun refreshWaterReadings() {
        _dismissedWaterStation.value = null
        val updated = _waterStations.value.map { station ->
            val delta = (Math.random() * 0.4) - 0.2
            station.copy(
                dissolvedOxygen = Math.round((station.dissolvedOxygen + delta).coerceIn(2.0, 11.0) * 10.0) / 10.0,
                bod = Math.round((station.bod + delta * 0.5).coerceIn(0.5, 20.0) * 10.0) / 10.0,
                ph = Math.round((station.ph + delta * 0.1).coerceIn(6.0, 9.5) * 10.0) / 10.0,
                lastUpdated = "Updated just now (Telemetry Sync)"
            )
        }
        _waterStations.value = updated
    }

    // --- Noise & Acoustics Monitoring State ---
    private val _noiseStations = MutableStateFlow<List<NoiseMonitoringStation>>(EnvironmentalMonitoringRepository.defaultNoiseStations)
    val noiseStations: StateFlow<List<NoiseMonitoringStation>> = _noiseStations.asStateFlow()

    private val _selectedNoiseStationId = MutableStateFlow(EnvironmentalMonitoringRepository.defaultNoiseStations.first().id)
    val selectedNoiseStationId: StateFlow<String> = _selectedNoiseStationId.asStateFlow()

    private val _selectedNoiseParameter = MutableStateFlow(NoiseParameterType.LEQ_DAY)
    val selectedNoiseParameter: StateFlow<NoiseParameterType> = _selectedNoiseParameter.asStateFlow()

    private val _selectedNoiseHourlyIndex = MutableStateFlow<Int?>(null)
    val selectedNoiseHourlyIndex: StateFlow<Int?> = _selectedNoiseHourlyIndex.asStateFlow()

    private val _noiseAlertsEnabled = MutableStateFlow(true)
    val noiseAlertsEnabled: StateFlow<Boolean> = _noiseAlertsEnabled.asStateFlow()

    private val _dismissedNoiseStation = MutableStateFlow<String?>(null)
    val dismissedNoiseStation: StateFlow<String?> = _dismissedNoiseStation.asStateFlow()

    fun selectNoiseStation(id: String) {
        _selectedNoiseStationId.value = id
        _selectedNoiseHourlyIndex.value = null
        _dismissedNoiseStation.value = null
    }

    fun selectNoiseParameter(param: NoiseParameterType) {
        _selectedNoiseParameter.value = param
        _selectedNoiseHourlyIndex.value = null
    }

    fun selectNoiseHourlyPoint(index: Int?) {
        _selectedNoiseHourlyIndex.value = index
    }

    fun toggleNoiseAlerts(enabled: Boolean) {
        _noiseAlertsEnabled.value = enabled
    }

    fun dismissNoiseAlert(stationId: String) {
        _dismissedNoiseStation.value = stationId
    }

    fun refreshNoiseReadings() {
        _dismissedNoiseStation.value = null
        val updated = _noiseStations.value.map { station ->
            val delta = (Math.random() * 2.0) - 1.0
            station.copy(
                leqDay = Math.round((station.leqDay + delta).coerceIn(35.0, 85.0) * 10.0) / 10.0,
                leqNight = Math.round((station.leqNight + delta * 0.8).coerceIn(30.0, 75.0) * 10.0) / 10.0,
                lmaxPeak = Math.round((station.lmaxPeak + delta * 1.5).coerceIn(45.0, 105.0) * 10.0) / 10.0,
                lastUpdated = "Updated just now (Telemetry Sync)"
            )
        }
        _noiseStations.value = updated
    }

    // --- Soil & Ecology Monitoring State ---
    private val _soilStations = MutableStateFlow<List<SoilMonitoringStation>>(EnvironmentalMonitoringRepository.defaultSoilStations)
    val soilStations: StateFlow<List<SoilMonitoringStation>> = _soilStations.asStateFlow()

    private val _selectedSoilStationId = MutableStateFlow(EnvironmentalMonitoringRepository.defaultSoilStations.first().id)
    val selectedSoilStationId: StateFlow<String> = _selectedSoilStationId.asStateFlow()

    private val _selectedSoilParameter = MutableStateFlow(SoilParameterType.SOIL_PH)
    val selectedSoilParameter: StateFlow<SoilParameterType> = _selectedSoilParameter.asStateFlow()

    private val _selectedSoilHourlyIndex = MutableStateFlow<Int?>(null)
    val selectedSoilHourlyIndex: StateFlow<Int?> = _selectedSoilHourlyIndex.asStateFlow()

    private val _soilAlertsEnabled = MutableStateFlow(true)
    val soilAlertsEnabled: StateFlow<Boolean> = _soilAlertsEnabled.asStateFlow()

    private val _dismissedSoilStation = MutableStateFlow<String?>(null)
    val dismissedSoilStation: StateFlow<String?> = _dismissedSoilStation.asStateFlow()

    fun selectSoilStation(id: String) {
        _selectedSoilStationId.value = id
        _selectedSoilHourlyIndex.value = null
        _dismissedSoilStation.value = null
    }

    fun selectSoilParameter(param: SoilParameterType) {
        _selectedSoilParameter.value = param
        _selectedSoilHourlyIndex.value = null
    }

    fun selectSoilHourlyPoint(index: Int?) {
        _selectedSoilHourlyIndex.value = index
    }

    fun toggleSoilAlerts(enabled: Boolean) {
        _soilAlertsEnabled.value = enabled
    }

    fun dismissSoilAlert(stationId: String) {
        _dismissedSoilStation.value = stationId
    }

    fun refreshSoilReadings() {
        _dismissedSoilStation.value = null
        val updated = _soilStations.value.map { station ->
            val delta = (Math.random() * 0.3) - 0.15
            station.copy(
                moisturePercent = Math.round((station.moisturePercent + delta * 2.0).coerceIn(10.0, 60.0) * 10.0) / 10.0,
                availableNitrogen = Math.round((station.availableNitrogen + delta * 1.5).coerceIn(10.0, 80.0) * 10.0) / 10.0,
                lastUpdated = "Updated just now (Telemetry Sync)"
            )
        }
        _soilStations.value = updated
    }

    val isAnyEnvironmentalThresholdExceeded: StateFlow<Boolean> = combine(
        _isAirQualityThresholdExceeded,
        _waterStations,
        _noiseStations,
        _soilStations
    ) { airExceeded, waters, noises, soils ->
        airExceeded || waters.any { it.isExceeded } || noises.any { it.isExceeded } || soils.any { it.isExceeded }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
