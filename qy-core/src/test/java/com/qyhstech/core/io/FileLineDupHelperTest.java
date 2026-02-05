package com.qyhstech.core.io;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyStr;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FileLineDupHelperTest {

    @Test
    public void test() {
//        List<String> listTests = Splitter.onPattern("[\r|\n|\\||,| |\t]").omitEmptyStrings().trimResults().splitToList("wangliyan@netconcepts.cn|\r" +
//                "christy@netconcepts.cn|zhengjian@netconcepts.cn,www.ok.com");

        List<String> listTests = QyStr.splitByRegex("wangliyan@netconcepts.cn|\r" +
                "christy@netconcepts.cn|zhengjian@netconcepts.cn,www.ok.com", "[\r|\n|\\||,| |\t]");

//        System.out.println(Joiner.on("---").join(listTests));
        System.out.println(StrUtil.join("---", listTests));
    }
}