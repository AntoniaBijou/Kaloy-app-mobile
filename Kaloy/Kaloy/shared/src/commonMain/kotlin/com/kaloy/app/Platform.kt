package com.kaloy.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform