package org.nikgor.project.data

import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.http.*


class OverpassClient {

    suspend fun queryPois(bbox: BoundingBox): List<Poi> {
        val bboxStr = "${bbox.south},${bbox.west},${bbox.north},${bbox.east}"

        val query = """
            [out:json][timeout:25];
            (
              node["tourism"="attraction"]($bboxStr);
              node["historic"]($bboxStr);
              node["tourism"="museum"]($bboxStr);
              node["leisure"="park"]($bboxStr);
            );
            out center;
        """.trimIndent()

        val response: OverpassResponse =
            HttpClientProvider.client.post("https://overpass-api.de/api/interpreter") {
                setBody(query)
                header(HttpHeaders.UserAgent, "marcheroute-kmp/0.1")
            }.body()

        return response.elements.mapNotNull {
            val lat = it.lat ?: it.center?.lat
            val lon = it.lon ?: it.center?.lon
            if (lat != null && lon != null) {
                Poi(
                    id = it.id,
                    lat = lat,
                    lon = lon,
                    name = it.tags?.get("name") ?: "(no name)"
                )
            } else null
        }
    }
}


@kotlinx.serialization.Serializable
private data class OverpassResponse(
    val elements: List<OverpassElement>
)

@kotlinx.serialization.Serializable
private data class OverpassElement(
    val id: Long,
    val lat: Double? = null,
    val lon: Double? = null,
    val center: Center? = null,
    val tags: Map<String, String>? = null
)

@kotlinx.serialization.Serializable
private data class Center(val lat: Double, val lon: Double)
