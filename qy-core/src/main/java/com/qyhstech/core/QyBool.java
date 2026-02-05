package com.qyhstech.core;

import java.util.Objects;

public class QyBool {

    /**
     * 不为空且为1
     *
     * @param dataId
     * @return
     */
    public static boolean isTrue(Long dataId) {
        return Objects.nonNull(dataId) && dataId == 1L;
    }

    /**
     * 不为空且为1
     *
     * @param dataId
     * @return
     */
    public static boolean isTrue(Integer dataId) {
        return Objects.nonNull(dataId) && dataId == 1;
    }

    /**
     * 不为空且等于0
     *
     * @param dataId
     * @return
     */
    public static boolean isFalse(Long dataId) {
        return Objects.nonNull(dataId) && dataId == 0L;
    }

    /**
     * 不为空且等于0
     *
     * @param dataId
     * @return
     */
    public static boolean isFalse(Integer dataId) {
        return Objects.nonNull(dataId) && dataId == 0;
    }
}
