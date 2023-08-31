package ru.hollowhorizon.fancychunks.core

@JvmRecord
data class UniformData(@get:JvmName("type") val type: String, @get:JvmName("name") val name: String)
