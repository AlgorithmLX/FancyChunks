package ru.hollowhorizon.fancychunks.mixin.chunk;

import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegionManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.hollowhorizon.fancychunks.core.ChunkAppearedLink;
import ru.hollowhorizon.fancychunks.ext.RenderRegionManagerExt;

@Mixin(value = RenderRegionManager.class, remap = false)
public class RenderRegionManagerMixin implements RenderRegionManagerExt {
    @Shadow @Final private Long2ReferenceOpenHashMap<RenderRegion> regions;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(CommandList commandList, CallbackInfo ci) {
        ChunkAppearedLink.setRegionManager(this);
    }

    @NotNull
    @Override
    public RenderRegion getRenderRegion(int chunkX, int chunkY, int chunkZ) {
        var key = RenderRegion.getRegionKeyForChunk(chunkX, chunkY, chunkZ);
        return regions.get(key);
    }
}
