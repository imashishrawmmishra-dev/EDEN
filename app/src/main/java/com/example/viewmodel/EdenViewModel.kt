package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.calculator.AirCalculatorEngine
import com.example.calculator.CarbonCalculatorEngine
import com.example.calculator.LiveCarbonTrackerEngine
import com.example.calculator.NoiseCalculatorEngine
import com.example.calculator.WaterCalculatorEngine
import com.example.data.model.CalculationEntity
import com.example.data.model.CompetencyEntity
import com.example.data.model.EdenAnswer
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity
import com.example.data.remote.EdenAiService
import com.example.data.repository.EdenRepository
import com.example.location.LiveLocationState
import com.example.location.LocationTracker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    PROFILE("Profile")
}

class EdenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EdenRepository(application)
    private val aiService = EdenAiService(repository)
    private val locationTracker = LocationTracker(application)

    private val _currentTab = MutableStateFlow(EdenTab.HOME)
    val currentTab: StateFlow<EdenTab> = _currentTab.asStateFlow()

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

    fun calculateAirStack(velocity: Double, diameter: Double, concMgM3: Double) {
        val result = AirCalculatorEngine.calculateStackFlow(velocity, diameter, concMgM3)
        _airResult.value = result

        viewModelScope.launch {
            repository.saveCalculation(
                CalculationEntity(
                    calculatorType = "Air Stack Emission Rate",
                    inputDescription = "Velocity: $velocity m/s, Diameter: $diameter m, Conc: $concMgM3 mg/Nm³",
                    resultSummary = "Flow: %.1f m³/h, Emission Rate: %.4f kg/h (%.4f g/s)".format(
                        result.flowRateM3Hour, result.emissionRateKgHour, result.emissionRateGSec
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
}
