package org.nikgor.project.data

import kotlinx.serialization.Serializable

@Serializable
data class CityLocation(val lat: Double, val lon: Double)

@Serializable
data class BoundingBox(
    val south: Double,
    val north: Double,
    val west: Double,
    val east: Double
)

@Serializable
data class Poi(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String
)

@Serializable
data class RoutePlan(
    val city: String,
    val center: CityLocation,
    val stops: List<Poi>
)
