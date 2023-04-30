package ru.hollowhorizon.fancychunks.ext;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;

import java.util.List;

public interface RenderRegionExt {
    void updateChunksFade(List<RenderSection> var1, ChunkShaderInterfaceExt var2, CommandList var3);
}

