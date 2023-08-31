package ru.hollowhorizon.fancychunks.mixin.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.hollowhorizon.fancychunks.core.ChunkAppearedLink;
import ru.hollowhorizon.fancychunks.util.MathUtils;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess {
    public LevelChunkMixin() {
        super(null, null, null, null, 0, null, null);
    }

    @Inject(
            method = "setBlockState",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;hasOnlyAir()Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void modifySetBlockStack(BlockPos p_62865_, BlockState p_62866_, boolean p_62867_, CallbackInfoReturnable<BlockState> cir, int i, LevelChunkSection levelchunksection) {
        if (!levelchunksection.hasOnlyAir()) return;

        ChunkPos chunkPos1 = getPos();
        ChunkAppearedLink.completeChunkFade(chunkPos1.x, MathUtils.floor(i / 16F), chunkPos1.z, true);
    }
}
