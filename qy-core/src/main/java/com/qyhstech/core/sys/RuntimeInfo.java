package com.qyhstech.core.sys;


import com.qyhstech.core.io.QyFile;

public class RuntimeInfo {

    private Runtime currentRuntime = Runtime.getRuntime();

    /**
     * 获得运行时对象
     *
     * @return {@link Runtime}
     */
    public final Runtime getRuntime() {
        return currentRuntime;
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getMaxMemory() {
        return currentRuntime.maxMemory();
    }

    /**
     * 获得JVM已分配内存
     *
     * @return 已分配内存
     */
    public final long getTotalMemory() {
        return currentRuntime.totalMemory();
    }

    /**
     * 获得JVM已分配内存中的剩余空间
     *
     * @return 已分配内存中的剩余空间
     */
    public final long getFreeMemory() {
        return currentRuntime.freeMemory();
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getUsableMemory() {
        return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        QySystemUtil.append(builder, "Max Memory:    ", QyFile.readableFileSize(getMaxMemory()));
        QySystemUtil.append(builder, "Total Memory:     ", QyFile.readableFileSize(getTotalMemory()));
        QySystemUtil.append(builder, "Free Memory:     ", QyFile.readableFileSize(getFreeMemory()));
        QySystemUtil.append(builder, "Usable Memory:     ", QyFile.readableFileSize(getUsableMemory()));

        return builder.toString();
    }
}
