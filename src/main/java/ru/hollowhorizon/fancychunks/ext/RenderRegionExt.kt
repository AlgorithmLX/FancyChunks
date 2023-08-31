package ru.hollowhorizon.fancychunks.ext

import me.jellysquid.mods.sodium.client.gl.device.CommandList
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection

interface RenderRegionExt {
    fun updateChunksFade(chunks: MutableList<RenderSection>, shader: ChunkShaderInterfaceExt, commandList: CommandList)
    fun getChunkData(x: Int, y: Int, z: Int): FloatArray
    fun competeChunkFade(x: Int, y: Int, z: Int, completeFade: Boolean)
}