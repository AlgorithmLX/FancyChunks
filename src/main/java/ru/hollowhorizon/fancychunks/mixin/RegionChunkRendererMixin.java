package ru.hollowhorizon.fancychunks.mixin;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.*;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.ext.RenderRegionExt;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(value = RegionChunkRenderer.class, remap = false)
public class RegionChunkRendererMixin {
    public RegionChunkRendererMixin() {
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RegionChunkRenderer;setModelMatrixUniforms(Lme/jellysquid/mods/sodium/client/render/chunk/shader/ChunkShaderInterface;Lme/jellysquid/mods/sodium/client/render/chunk/region/RenderRegion;Lme/jellysquid/mods/sodium/client/render/chunk/ChunkCameraContext;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyChunkRender(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderList list, BlockRenderPass pass, ChunkCameraContext camera, CallbackInfo ci, ChunkShaderInterface shader, Iterator i, Map.Entry e, RenderRegion region, List<RenderSection> chunks) {
        ChunkShaderInterfaceExt ext = (ChunkShaderInterfaceExt) shader;
        RenderRegionExt regionExt = (RenderRegionExt) region;
        regionExt.updateChunksFade(chunks, ext, commandList);

    }
}

