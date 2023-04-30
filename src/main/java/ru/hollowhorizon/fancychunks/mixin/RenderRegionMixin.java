package ru.hollowhorizon.fancychunks.mixin;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.hollowhorizon.fancychunks.ext.ChunkFadeInController;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.ext.RenderRegionExt;

import java.util.List;

@Mixin(value = RenderRegion.class, remap = false)
public class RenderRegionMixin implements RenderRegionExt {
    private ChunkFadeInController fadeController;

    public RenderRegionMixin() {
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyConstructor(RenderRegionManager manager, int x, int y, int z, CallbackInfo ci) {
        this.fadeController = new ChunkFadeInController();
    }

    @Inject(method = "removeChunk", at = @At("TAIL"))
    private void modifyRemoveChunk(RenderSection chunk, CallbackInfo ci) {
        this.fadeController.resetFadeForChunk(chunk.getChunkId());
    }

    @Inject(method = "deleteResources", at = @At("TAIL"))
    private void modifyDeleteResources(CommandList commandList, CallbackInfo ci) {
        this.fadeController.delete(commandList);
    }

    public void updateChunksFade(List<RenderSection> chunks, ChunkShaderInterfaceExt shader, CommandList commandList) {
        this.fadeController.updateChunksFade(chunks, shader, commandList);
    }
}
