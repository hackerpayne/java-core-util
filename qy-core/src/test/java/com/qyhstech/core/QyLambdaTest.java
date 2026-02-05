package com.qyhstech.core;

import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.domain.dto.ModelPage;
import org.junit.jupiter.api.Test;

class QyLambdaTest {

    @Test
    void resolve() {
        ModelArticle article = new ModelArticle();
        QyLambda.resolve(article::getAuthor).ifPresent(System.out::println);
    }

    @Test
    void getFieldName() {
        String fieldName = QyLambda.getFieldName(ModelPage::getCurPage);
        System.out.println(fieldName);
    }

    @Test
    void getFieldValue() {
        ModelArticle article = new ModelArticle();
        article.setArtid(1);
        article.setBody("test");
        Integer fieldValue = QyLambda.getFieldValue(article, ModelArticle::getArtid);
        System.out.println(fieldValue);
    }
}