package ru.hollowhorizon.fancychunks.mixin.oculus;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.shader.GlProgram;
import me.jellysquid.mods.sodium.client.render.chunk.*;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderChunkRendererExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.ext.RenderRegionExt;
import ru.hollowhorizon.fancychunks.hook.OculusApiHook;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(value = RegionChunkRenderer.class, remap = false)
public abstract class OculusRegionChunkRendererMixin implements ShaderChunkRendererExt {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/RegionChunkRenderer;setModelMatrixUniforms(Lme/jellysquid/mods/sodium/client/render/chunk/shader/ChunkShaderInterface;Lme/jellysquid/mods/sodium/client/render/chunk/region/RenderRegion;Lme/jellysquid/mods/sodium/client/render/chunk/ChunkCameraContext;)V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void modifyChunkRender(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderList list, BlockRenderPass pass, ChunkCameraContext camera, CallbackInfo ci, ChunkShaderInterface shader, Iterator var7, Map.Entry entry, RenderRegion region, List<RenderSection> chunks) {
        if (!OculusApiHook.isOculusShaderPackInUse()) return;

        GlProgram<IrisChunkShaderInterface> override = iris$getOverride();
        if (override == null) return;

        final ChunkShaderInterfaceExt ext = (ChunkShaderInterfaceExt) (override.getInterface());
        final RenderRegionExt renderRegionExt = (RenderRegionExt) region;
        renderRegionExt.updateChunksFade(chunks, ext, commandList);
    }
}
