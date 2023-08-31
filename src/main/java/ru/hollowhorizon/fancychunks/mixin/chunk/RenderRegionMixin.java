package ru.hollowhorizon.fancychunks.mixin.chunk;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegionManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.hollowhorizon.fancychunks.core.ChunkFadeInController;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.ext.RenderRegionExt;

import java.util.List;

@Mixin(value = RenderRegion.class, remap = false, priority = 1)
public class RenderRegionMixin implements RenderRegionExt {
    @Unique
    private ChunkFadeInController fadeController;

    public RenderRegionMixin() {
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyConstructor(RenderRegionManager manager, int x, int y, int z, CallbackInfo ci) {
        this.fadeController = new ChunkFadeInController();
    }

    @Inject(method = "addChunk", at = @At(value = "TAIL"))
    private void modifyAddChunk(RenderSection chunk, CallbackInfo ci) {
//        if (!Config.isModEnabled)
//            return;

//        boolean completeAnimation = false;
//        if (!Config.animateNearPlayer) {
//            final int chunkX = chunk.getChunkX();
//            final int chunkY = chunk.getChunkY();
//            final int chunkZ = chunk.getChunkZ();
//
//            Entity camera = MinecraftClient.getInstance().cameraEntity;
//            if (camera != null) {
//                final int camChunkX = MathUtils.floor((float) (camera.lastRenderX / 16));
//                final int camChunkY = MathUtils.floor((float) (camera.lastRenderY / 16));
//                final int camChunkZ = MathUtils.floor((float) (camera.lastRenderZ / 16));
//
//                if (MathUtils.chunkInRange(chunkX, chunkY, chunkZ, camChunkX, camChunkY, camChunkZ, 1))
//                    completeAnimation = true;
//            }
//        }

//        if (!completeAnimation)
//            fadeController.resetFadeForChunk(chunk.getChunkId());
//        else
            fadeController.completeChunkFade(chunk.getChunkId(), false);
    }

    @Inject(method = "deleteResources", at = @At("TAIL"))
    private void modifyDeleteResources(CommandList commandList, CallbackInfo ci) {
        this.fadeController.delete(commandList);
    }

    @Override
    public void updateChunksFade(List<RenderSection> chunks, ChunkShaderInterfaceExt shader, CommandList commandList) {
        this.fadeController.updateChunksFade(chunks, shader, commandList);
    }

    @Override
    public float[] getChunkData(int x, int y, int z) {
        return fadeController.getChunkData(x, y, z);
    }

    @Override
    public void competeChunkFade(int x, int y, int z, boolean completeFade) {
        fadeController.completeChunkFade(x, y, z, completeFade);
    }
}
