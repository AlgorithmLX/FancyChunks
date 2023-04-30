package ru.hollowhorizon.fancychunks.ext;

import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkFadeInController {
    private final Map<Integer, Float> progressMap = new HashMap<>();
    private final DataBuffer chunkFadeDatasBuffer = new DataBuffer(RenderRegion.REGION_SIZE, 4);
    private GlMutableBuffer chunkGlFadeDataBuffer;

    private long lastFrameTime = 0L;

    public ChunkFadeInController() {
        for (int i = 0; i < RenderRegion.REGION_SIZE; i++)
            resetFadeForChunk(i);
    }

    public void resetFadeForChunk(int chunkId) {
        chunkFadeDatasBuffer.put(chunkId, 0, 0f);
        chunkFadeDatasBuffer.put(chunkId, 1, -64F);
        chunkFadeDatasBuffer.put(chunkId, 2, 0f);
        chunkFadeDatasBuffer.put(chunkId, 3, 0f);
        progressMap.remove(chunkId);
    }

    public void updateChunksFade(List<RenderSection> chunks, ChunkShaderInterfaceExt shader, CommandList commandList) {
        checkMutableBuffer(commandList);

        final long currentFrameTime = ZonedDateTime.now().toInstant().toEpochMilli();
        final float delta = lastFrameTime == 0L ? 0 : currentFrameTime - lastFrameTime;

        for (RenderSection chunk : chunks)
            processChunk(delta, chunk);

        chunkFadeDatasBuffer.uploadData(commandList, chunkGlFadeDataBuffer);
        shader.setFadeDatas(chunkGlFadeDataBuffer);
        lastFrameTime = currentFrameTime;
    }

    public void delete(CommandList commandList) {
        chunkFadeDatasBuffer.delete();

        if (chunkGlFadeDataBuffer != null)
            commandList.deleteBuffer(chunkGlFadeDataBuffer);
    }

    private void checkMutableBuffer(CommandList commandList) {
        if (chunkGlFadeDataBuffer == null)
            chunkGlFadeDataBuffer = commandList.createMutableBuffer();
    }

    private void processChunk(final float delta, RenderSection chunk) {
        final int chunkId = chunk.getChunkId();

        final float fadeCoeffChange = 0.032f;
        float fadeCoeff = chunkFadeDatasBuffer.get(chunkId, 3);

        if (fadeCoeff != 1f) {
            fadeCoeff += fadeCoeffChange;

            if (fadeCoeff > 1f)
                fadeCoeff = 1f;

            chunkFadeDatasBuffer.put(chunkId, 3, fadeCoeff);
        }

//        if (Config.isAnimationEnabled) {
//            float y = chunkFadeDatasBuffer.get(chunkId, 1);
//            if (y != 0f) {
//                Float animationProgress = progressMap.remove(chunkId);
//                if (animationProgress == null)
//                    animationProgress = 0f;
//
//                animationProgress += Config.animationChangePerMs * delta;
//                if (animationProgress > 1f)
//                    animationProgress = 1f;
//
//                progressMap.put(chunkId, animationProgress);
//
//                float curved = Config.animationInitialOffset
//                        - Config.animationCurve.calculate(animationProgress) * Config.animationInitialOffset;
//                curved = -curved;
//
//                if (y <= 0f && curved > 0f) {
//                    y = 0f;
//                    progressMap.remove(chunkId);
//                } else
//                    y = curved;
//
//                chunkFadeDatasBuffer.put(chunkId, 1, y);
//            }
//        } else
            if (chunkFadeDatasBuffer.get(chunkId, 1) != 0f) chunkFadeDatasBuffer.put(chunkId, 1, 0f);
    }
}
