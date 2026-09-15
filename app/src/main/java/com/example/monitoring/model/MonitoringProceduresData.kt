package com.example.monitoring.model

/**
 * 5 Standardized Monitoring Domains specifically requested for procedures and PDF field data sheets:
 * 1. Ambient Air Quality Monitoring
 * 2. Indoor Air Quality Monitoring
 * 3. Acoustic & Noise Monitoring
 * 4. Stack Emission Monitoring
 * 5. Water Quality Parameters Monitoring
 */
enum class MonitoringProcedureDomain(
    val id: String,
    val title: String,
    val shortName: String,
    val isoStandard: String,
    val usepaStandard: String,
    val localStandard: String, // CPCB / EU / AS/NZS
    val primaryParameter: String,
    val fdsDocNumber: String
) {
    AMBIENT(
        id = "ambient",
        title = "Ambient Air Quality Monitoring",
        shortName = "Ambient Air",
        isoStandard = "ISO 12341:2023 & ISO 21501-4",
        usepaStandard = "USEPA 40 CFR Part 50 (App B, J, L)",
        localStandard = "CPCB NAAQS 2009 / EU 2008/50/EC / AS/NZS 3580",
        primaryParameter = "PM2.5 & PM10 Particulate Matter",
        fdsDocNumber = "EDEN-QMS-FDS-AAQ-01 / Rev 4.2"
    ),
    INDOOR(
        id = "indoor",
        title = "Indoor Air Quality Monitoring (IAQ)",
        shortName = "Indoor Air",
        isoStandard = "ISO 16000-1/2/3/6 & ISO 16017",
        usepaStandard = "USEPA IAQ Protocol & ASHRAE 62.1/55",
        localStandard = "EN 16798-1 / ISHRAE IEQ / OSHA IAQ",
        primaryParameter = "CO₂, TVOC, HCHO & Ventilation Rate",
        fdsDocNumber = "EDEN-QMS-FDS-IAQ-02 / Rev 3.1"
    ),
    NOISE(
        id = "noise",
        title = "Acoustic & Noise Level Monitoring",
        shortName = "Noise Level",
        isoStandard = "ISO 1996-1:2016 & ISO 1996-2:2017",
        usepaStandard = "USEPA Noise Control Act (EPA 550/9-74-004)",
        localStandard = "CPCB Noise Rules 2000 / BS 4142 / AS 1055",
        primaryParameter = "Leq, Lmax, L10, L90 & Ldn (dBA)",
        fdsDocNumber = "EDEN-QMS-FDS-NOI-03 / Rev 2.5"
    ),
    STACK(
        id = "stack",
        title = "Stack Emission Monitoring (Isokinetic)",
        shortName = "Stack Emission",
        isoStandard = "ISO 9096:2017 & ISO 12141:2002",
        usepaStandard = "USEPA Methods 1, 2, 3, 4, 5 & 17 (40 CFR 60)",
        localStandard = "CPCB Emission Guidelines / BS EN 13284-1 / AS 4323.2",
        primaryParameter = "Particulate Matter (mg/Nm³) & Gas Velocity",
        fdsDocNumber = "EDEN-QMS-FDS-STK-04 / Rev 5.0"
    ),
    WATER(
        id = "water",
        title = "Water Quality Parameters Monitoring",
        shortName = "Water Quality",
        isoStandard = "ISO 5667-1/3/6/14 & ISO 10523",
        usepaStandard = "USEPA NPDES 40 CFR Part 136 & APHA 24th Ed.",
        localStandard = "CPCB Schedule VI / EU WFD 2000/60/EC / IS 10500",
        primaryParameter = "pH, DO, BOD₅, COD, TDS & Turbidity",
        fdsDocNumber = "EDEN-QMS-FDS-WTR-05 / Rev 3.4"
    )
}

/**
 * Standardized Benchmark comparison between ISO, USEPA, and Local Standards
 */
data class RegulatoryBenchmarkRow(
    val parameter: String,
    val isoLimit: String,
    val usepaLimit: String,
    val localLimit: String,
    val unit: String,
    val averagingTime: String
)

/**
 * Comprehensive Standard Operating Procedure Document
 */
data class MonitoringProcedureDoc(
    val domain: MonitoringProcedureDomain,
    val title: String,
    val regulatoryCode: String,
    val isoStandardRef: String,
    val usepaStandardRef: String,
    val localStandardRef: String,
    val scopeAndObjective: String,
    val principleOfMethod: String,
    val requiredEquipmentAndReagents: List<String>,
    val preSamplingVerificationAndLeakCheck: List<String>,
    val samplingExecutionProtocol: List<String>,
    val sampleHandlingAndPreservation: List<String>,
    val qaQcProtocols: List<String>,
    val governingFormulas: List<Pair<String, String>>,
    val regulatoryBenchmarks: List<RegulatoryBenchmarkRow>
)

/**
 * Field Data Sheet Form Data Model
 */
data class FdsProjectInfo(
    val facilityName: String = "Stark Energy & Refining Complex",
    val siteLocation: String = "North Industrial Corridor, Plot 42-B",
    val stationId: String = "AAQ-STATION-01",
    val monitoringDate: String = "15-Sep-2026",
    val technicianName: String = "Dr. Robert Vance, Lead Field Chemist",
    val reviewerName: String = "Elena Rostova, QA/QC Technical Manager",
    val ambientTemp: String = "26.5",
    val barometricPressure: String = "758.0",
    val relativeHumidity: String = "48.0",
    val weatherCondition: String = "Clear, Wind NW at 2.4 m/s"
)

data class FdsInstrumentInfo(
    val instrumentModel: String,
    val serialNumber: String,
    val calibrationCertNumber: String,
    val calibrationValidUntil: String,
    val preCalReading: String,
    val postCalReading: String,
    val calibrationDrift: String
)

data class FdsSamplingRunRow(
    val pointOrTime: String,
    val param1: String,
    val param2: String,
    val param3: String,
    val param4: String,
    val remarks: String
)

data class FieldDataSheetTemplate(
    val domain: MonitoringProcedureDomain,
    val formTitle: String,
    val documentNumber: String,
    val accreditationNotice: String,
    val defaultProjectInfo: FdsProjectInfo,
    val defaultInstrumentInfo: FdsInstrumentInfo,
    val preSamplingChecklist: List<String>,
    val runTableHeaders: List<String>,
    val sampleRunData: List<FdsSamplingRunRow>,
    val calculations: List<Pair<String, String>>,
    val measuredResult: String,
    val regulatoryVerdict: String,
    val isCompliant: Boolean
)
