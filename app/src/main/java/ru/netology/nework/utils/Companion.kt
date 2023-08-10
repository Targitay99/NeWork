package ru.netology.nework.utils

import android.os.Bundle


class Companion {

    companion object {
        var Bundle.latitude: Double
            set(value) = putDouble("PointLat", value)
            get() = getDouble("PointLat")
        var Bundle.longitude: Double
            set(value) = putDouble("PointLong", value)
            get() = getDouble("PointLong")
        var Bundle.idPoint: Int
            set(value) = putInt("PointId", value)
            get() = getInt("PointId")
        var Bundle.zoom: Float
            set(value) = putFloat("PointZoom", value)
            get() = getFloat("PointZoom")
        var Bundle.latitudeNew: Double
            set(value) = putDouble("PointLat", value)
            get() = getDouble("PointLat")
        var Bundle.longitudeNew: Double
            set(value) = putDouble("PointLong", value)
            get() = getDouble("PointLong")
    }
}