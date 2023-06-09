package ru.hollowhorizon.fancychunks.ext;

import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferUsage;
import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class DataBuffer {
    private final ByteBuffer buffer;
    private final int stride;
    private final int fieldsCount;

    public DataBuffer(int size, int fieldsCount) {
        this.stride = fieldsCount * 4;
        this.fieldsCount = fieldsCount;

        buffer = MemoryUtil.memAlloc(size * stride);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < fieldsCount; j++)
                put(i, j, 0f);
    }

    public void put(int index, int fieldNum, float data) {
        buffer.putFloat(getPosition(index, fieldNum), data);
    }

    public float get(int index, int fieldNum) {
        return buffer.getFloat(getPosition(index, fieldNum));
    }

    public void uploadData(CommandList commandList, GlMutableBuffer glBuffer) {
        commandList.uploadData(glBuffer, buffer, GlBufferUsage.DYNAMIC_DRAW);
    }

    public void reset(int i) {
        for (int j = 0; j < fieldsCount; j++)
            put(i, j, 0f);
    }

    public void delete() {
        buffer.clear();
    }

    private int getPosition(int index, int fieldNum) {
        return index * stride + fieldNum * 4;
    }
}
