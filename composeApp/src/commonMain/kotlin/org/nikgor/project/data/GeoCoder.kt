package org.nikgor.project.data

import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.http.*

class GeoCoder {

    suspend fun geocode(city: String): Pair<CityLocation, BoundingBox> {
        val response: List<NominatimResponse> =
            HttpClientProvider.client.get("https://nominatim.openstreetmap.org/search") {
                parameter("q", city)
                parameter("format", "json")
                parameter("limit", 1)
                header(HttpHeaders.UserAgent, "marcheroute-kmp/0.1")
            }.body()

        if (response.isEmpty()) {
            error("City not found")
        }

        val r = response.first()

        val bbox = BoundingBox(
            south = r.boundingbox[0].toDouble(),
            north = r.boundingbox[1].toDouble(),
            west = r.boundingbox[2].toDouble(),
            east = r.boundingbox[3].toDouble()
        )

        return CityLocation(r.lat.toDouble(), r.lon.toDouble()) to bbox
    }
}

@kotlinx.serialization.Serializable
private data class NominatimResponse(
    val lat: String,
    val lon: String,
    val boundingbox: List<String>
)
