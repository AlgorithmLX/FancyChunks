package ru.hollowhorizon.fancychunks.mixin.entity;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.hollowhorizon.fancychunks.core.ChunkAppearedLink;
import ru.hollowhorizon.fancychunks.core.ChunkUtils;
import ru.hollowhorizon.fancychunks.util.MathUtils;

@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class SodiumWorldRendererMixin {
    @Inject(
            method = "isEntityVisible",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injRenderBE(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            var chunkPos = entity.chunkPosition();
            var chunkY = MathUtils.floor((float) entity.getY() / 16F);

            var fadeFata = ChunkAppearedLink.getChunkData(chunkPos.x, chunkY, chunkPos.z);

            var isVisible = !(fadeFata[1] == -64 && fadeFata[2] == 0F);

            if (!isVisible) {
                var chunk = ChunkUtils.getChunkOn(entity.getLevel(), chunkPos, chunkY);
                if (chunk == null || chunk.hasOnlyAir())
                    isVisible = true;
            }

            cir.setReturnValue(isVisible);
        }
    }
}
