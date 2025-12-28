package org.nikgor.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform