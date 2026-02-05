package com.qyhstech.core.util;

import cn.hutool.core.collection.CollUtil;
import com.qyhstech.core.QyRand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RandomUtilTest {
    @Test
    public void randomEleSetTest() {
        Set<Integer> set = QyRand.getRandUniqueFromList(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
        Assertions.assertEquals(set.size(), 2);
    }
}
