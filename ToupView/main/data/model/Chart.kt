package com.example.astroclient.model

data class Chart(
    var dx: Double? = null,
    var dy: Double? = null,
    var RADistanceRaw: Double? = null,
    var DecDistanceRaw: Double? = null,
    var RADistanceGuide: Double? = null,
    var DecDistanceGuide: Double? = null,
    var RADuration: Int? = null,
    var RADirection: String? = null,
    var DECDuration: Int? = null,
    var DECDirection: String? = null,
    var StarMass: Double? = null,
    var SNR: Double? = null,
    var HFD: Double? = null,
    var AvgDist: Double? = null,
    )
