package com.qyhstech.core.crypto;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

class QyMd5Test {

    @Test
    void md5() {
        System.out.println(QyMd5.md5("hahah"));
    }

    @Test
    void md52() {
        System.out.println(QyMd5.md5(StrUtil.bytes("hahah")));
    }

    @Test
    void md516() {
        System.out.println(QyMd5.md516("hhdhdhdhddh"));
    }

    @Test
    void mapMd5SignCheck() {
    }
}