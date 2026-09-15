package com.example

import com.example.monitoring.data.PollutionMonitoringRepository
import com.example.monitoring.engine.PollutionCalculationEngine
import com.example.monitoring.model.ComplianceStatus
import com.example.monitoring.model.MonitoringDuration
import com.example.monitoring.model.PollutionMonitoringDomain
import com.example.monitoring.model.RegulatoryStandard
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun verifyAll11DomainsHaveSOPsAndManuals() {
        assertEquals(11, PollutionMonitoringDomain.values().size)

        PollutionMonitoringDomain.values().forEach { domain ->
            val sop = PollutionMonitoringRepository.getStandardProcedure(domain)
            assertNotNull("SOP standardTitle must not be null for $domain", sop.standardTitle)
            assertTrue("Pre-sampling must not be empty for $domain", sop.preSamplingProtocol.isNotEmpty())
            assertTrue("Leak check must not be empty for $domain", sop.instrumentSetupAndLeakCheck.isNotEmpty())
            assertTrue("Sampling execution must not be empty for $domain", sop.samplingExecutionProtocol.isNotEmpty())
            assertTrue("Sample handling must not be empty for $domain", sop.sampleHandlingAndPreservation.isNotEmpty())
            assertTrue("Math formula must not be empty for $domain", sop.mathCalculationFormula.isNotBlank())

            val manual = PollutionMonitoringRepository.getInstrumentManual(domain)
            assertNotNull("Instrument manual must not be null for $domain", manual)
            assertTrue("Operating principle must not be empty for $domain", manual.operatingPrinciple.isNotBlank())
            assertTrue("Calibration protocol must not be empty for $domain", manual.calibrationProtocol.isNotEmpty())
            assertTrue("Zero/span procedure must not be empty for $domain", manual.zeroAndSpanProcedure.isNotEmpty())
            assertTrue("Maintenance protocol must not be empty for $domain", manual.maintenanceAndServicing.isNotEmpty())

            val fields = PollutionMonitoringRepository.getFieldDefinitions(domain)
            assertTrue("Field definitions must not be empty for $domain", fields.isNotEmpty())
        }
    }

    @Test
    fun verifyAll8StandardsHaveBenchmarksForAll11Domains() {
        assertEquals(8, RegulatoryStandard.values().size)

        PollutionMonitoringDomain.values().forEach { domain ->
            RegulatoryStandard.values().forEach { standard ->
                MonitoringDuration.values().forEach { duration ->
                    val benchmark = PollutionMonitoringRepository.getStandardBenchmark(domain, standard, duration)
                    assertNotNull("Benchmark must exist for $domain, $standard, $duration", benchmark)
                    assertTrue("Benchmark limit must be greater than 0 for $domain, $standard, $duration", benchmark.limitValue > 0.0)
                    assertTrue("Benchmark unit must be present", benchmark.unit.isNotBlank())
                    assertTrue("Citation rule must be present", benchmark.citationRule.isNotBlank())
                }
            }
        }
    }

    @Test
    fun verifyLiveCalculationEngineAcrossAllDomains() {
        PollutionMonitoringDomain.values().forEach { domain ->
            val fieldDefs = PollutionMonitoringRepository.getFieldDefinitions(domain)
            val defaultInputs = fieldDefs.associate { it.id to it.defaultValue }

            val result = PollutionCalculationEngine.calculate(
                domain = domain,
                standard = RegulatoryStandard.USEPA,
                duration = MonitoringDuration.TWENTY_FOUR_HOUR,
                inputs = defaultInputs
            )

            assertNotNull("Result should not be null for $domain", result)
            assertTrue("Main calculated value should be positive for $domain", result.mainCalculatedValue >= 0.0)
            assertTrue("Step by step formulas should not be empty for $domain", result.stepByStepFormulas.isNotEmpty())
            assertNotNull("Compliance status should be determined for $domain", result.complianceStatus)
            assertTrue("Compliance summary should be present for $domain", result.complianceSummary.isNotBlank())
        }
    }

    @Test
    fun verifyExceedanceDetection() {
        // High particulate in ambient air
        val highDustInputs = mapOf(
            "initial_flow" to "16.67",
            "final_flow" to "16.67",
            "duration_minutes" to "1440",
            "initial_filter_wt" to "100.0",
            "final_filter_wt" to "105.0", // 5.0 mg in 24m3 = ~208 ug/m3 (well above 35 limit)
            "ambient_temp" to "25.0",
            "ambient_pressure" to "760.0"
        )

        val result = PollutionCalculationEngine.calculate(
            domain = PollutionMonitoringDomain.AMBIENT_AIR,
            standard = RegulatoryStandard.USEPA,
            duration = MonitoringDuration.TWENTY_FOUR_HOUR,
            inputs = highDustInputs
        )

        assertEquals(ComplianceStatus.EXCEEDED, result.complianceStatus)
        assertTrue(result.mainCalculatedValue > result.thresholdLimit)
    }

    @Test
    fun verifyEpaMethod2StackCalculationWithTemperature() {
        val velocity = 15.0 // m/s
        val diameter = 2.0  // m
        val concentration = 50.0 // mg/m3
        val tempC = 165.0 // °C

        val result = com.example.calculator.AirCalculatorEngine.calculateStackFlow(
            velocityMPerSec = velocity,
            diameterMeters = diameter,
            pollutantConcMgPerM3 = concentration,
            tempCelsius = tempC
        )

        // Area = pi * 1^2 = ~3.14159 m2
        assertEquals(Math.PI, result.ductAreaM2, 0.001)
        // Actual Flow = 15 * pi = ~47.1238 m3/s
        assertEquals(velocity * Math.PI, result.flowRateM3Sec, 0.01)
        // Flow m3/hr = ~169,646 m3/hr
        assertEquals(velocity * Math.PI * 3600.0, result.flowRateM3Hour, 1.0)
        // Normalized flow Q_std = Q * (293.15 / (165 + 273.15)) = Q * (293.15 / 438.15) = ~0.669 * Q
        val expectedNormFlow = (velocity * Math.PI * 3600.0) * (293.15 / (tempC + 273.15))
        assertEquals(expectedNormFlow, result.normalizedFlowRateM3Hour, 1.0)
        assertEquals(tempC, result.stackTempCelsius, 0.001)
        assertTrue(result.normalizedFlowRateM3Hour < result.flowRateM3Hour)
    }

    @Test
    fun verifyEpaMethod2PlausibilityBoundsOnStackFields() {
        val fields = PollutionMonitoringRepository.getFieldDefinitions(PollutionMonitoringDomain.STACK_EMISSION)
        val diameterField = fields.firstOrNull { it.id == "stack_diameter" }
        val tempField = fields.firstOrNull { it.id == "stack_temp" }

        assertNotNull("stack_diameter field must exist", diameterField)
        assertNotNull("stack_temp field must exist", tempField)

        // Diameter: 0.10 m to 15.0 m
        assertEquals(0.10, diameterField!!.minPlausible!!, 0.001)
        assertEquals(15.0, diameterField.maxPlausible!!, 0.001)
        assertNotNull(diameterField.validationTooltip)
        assertTrue(diameterField.validationTooltip!!.contains("EPA Method 1"))

        // Temperature: -50 °C to 1200 °C
        assertEquals(-50.0, tempField!!.minPlausible!!, 0.001)
        assertEquals(1200.0, tempField.maxPlausible!!, 0.001)
        assertNotNull(tempField.validationTooltip)
        assertTrue(tempField.validationTooltip!!.contains("EPA Method 2"))
    }

    @Test
    fun verifyEnvironmentalJobPortalsAllSpecifiedWebsitesPresent() {
        val portals = com.example.data.model.EnvironmentalJobRepository.portals
        val portalIds = portals.map { it.id }.toSet()

        // Verify Global Aggregators
        assertTrue("Indeed must be present", portalIds.contains("indeed"))
        assertTrue("LinkedIn must be present", portalIds.contains("linkedin"))
        assertTrue("ZipRecruiter must be present", portalIds.contains("ziprecruiter"))
        assertTrue("Glassdoor must be present", portalIds.contains("glassdoor"))
        assertTrue("CareerBuilder must be present", portalIds.contains("careerbuilder"))

        // Verify Middle East & UAE Specific Portals
        assertTrue("Bayt must be present", portalIds.contains("bayt"))
        assertTrue("GulfTalent must be present", portalIds.contains("gulftalent"))
        assertTrue("Naukrigulf must be present", portalIds.contains("naukrigulf"))

        // Verify Tech & Startup Niches
        assertTrue("Wellfound must be present", portalIds.contains("wellfound"))
        assertTrue("Dice must be present", portalIds.contains("dice"))

        // Verify Remote & Freelance Markets
        assertTrue("FlexJobs must be present", portalIds.contains("flexjobs"))
        assertTrue("Upwork must be present", portalIds.contains("upwork"))

        // Verify Government Channels
        assertTrue("USAJOBS must be present", portalIds.contains("usajobs"))

        // Verify NGO & Non-Profit channels
        assertTrue("Idealist must be present", portalIds.contains("idealist"))
        assertTrue("UN Careers must be present", portalIds.contains("un_careers"))
    }

    @Test
    fun verifyEnvironmentalDirectSearchUrlGeneration() {
        val portals = com.example.data.model.EnvironmentalJobRepository.portals

        val indeed = portals.first { it.id == "indeed" }
        val indeedUrl = indeed.buildSearchUrl("Air Quality Specialist", "UAE")
        assertTrue(indeedUrl.contains("indeed.com/jobs?q="))
        assertTrue(indeedUrl.contains("environmental"))

        val bayt = portals.first { it.id == "bayt" }
        val baytUrl = bayt.buildSearchUrl("Emissions Engineer")
        assertTrue(baytUrl.contains("bayt.com/en/search-jobs/?keyword="))

        val usajobs = portals.first { it.id == "usajobs" }
        val usajobsUrl = usajobs.buildSearchUrl("Protection Specialist")
        assertTrue(usajobsUrl.contains("usajobs.gov/Search/Results?k="))

        val wellfound = portals.first { it.id == "wellfound" }
        val wellfoundUrl = wellfound.buildSearchUrl("CleanTech")
        assertTrue(wellfoundUrl.contains("wellfound.com/jobs?keyword="))
    }

    @Test
    fun verifyEnvironmentalSectorsCoverNgoPublicPrivate() {
        val curatedOpenings = com.example.data.model.EnvironmentalJobRepository.curatedOpenings
        val sectors = curatedOpenings.map { it.sectorType }.toSet()

        assertTrue(sectors.contains(com.example.data.model.JobSectorType.NGO))
        assertTrue(sectors.contains(com.example.data.model.JobSectorType.PUBLIC))
        assertTrue(sectors.contains(com.example.data.model.JobSectorType.PRIVATE))
    }
}

