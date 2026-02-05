package com.qyhstech.core.util;

import com.qyhstech.core.domain.page.QyPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 分页单元测试
 *
 * @author Looly
 */
public class PageUtilTest {

    @Test
    public void transToStartEndTest() {
        int[] startEnd1 = QyPage.transToStartEnd(1, 10);
        Assertions.assertEquals(0, startEnd1[0]);
        Assertions.assertEquals(10, startEnd1[1]);

        int[] startEnd2 = QyPage.transToStartEnd(2, 10);
        Assertions.assertEquals(10, startEnd2[0]);
        Assertions.assertEquals(20, startEnd2[1]);
    }

    @Test
    public void totalPage() {
        int totalPage = QyPage.totalPage(20, 3);
        Assertions.assertEquals(7, totalPage);
    }
}
