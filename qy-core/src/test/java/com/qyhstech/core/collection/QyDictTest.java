package com.qyhstech.core.collection;

import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.json.QyJackson;
import org.junit.jupiter.api.Test;

import java.util.List;

class QyDictTest {

    @Test
    void buildDict() {

        List<ModelArticle> articleList = QyList.empty();
        articleList.add(ModelArticle.builder().title("标题1").artid(1).body("0").build());
        articleList.add(ModelArticle.builder().title("标题2").artid(2).body("1").build());
        articleList.add(ModelArticle.builder().title("标题3").artid(3).body("2").build());

        var dict = QyDict.buildDict(articleList, ModelArticle::getTitle, ModelArticle::getArtid);
        System.out.println(QyJackson.toJsonString(dict));
    }

    @Test
    void testBuildDict() {
        List<ModelArticle> articleList = QyList.empty();
        articleList.add(ModelArticle.builder().title("标题1").artid(1).body("0").build());
        articleList.add(ModelArticle.builder().title("标题2").artid(2).body("1").build());
        articleList.add(ModelArticle.builder().title("标题3").artid(3).body("2").build());

        var dict = QyDict.buildDict(articleList, ModelArticle::getTitle, ModelArticle::getBody);
        System.out.println(QyJackson.toJsonString(dict));
    }

    @Test
    void testBuildDict1() {
    }

    @Test
    void buildDictTree() {
    }

    @Test
    void testBuildDictTree() {
    }
}