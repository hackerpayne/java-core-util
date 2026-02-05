package com.qyhstech.core;

import org.junit.jupiter.api.Test;

import java.util.List;

class QyStrTest {

    @Test
    void isNull() {
    }

    @Test
    void notNull() {
    }

    @Test
    void filterEmoji() {
    }

    @Test
    void testFilterEmoji() {
    }

    @Test
    void addStart() {
    }

    @Test
    void hideName() {
    }

    @Test
    void hideIdcard() {
    }

    @Test
    void hideMobile() {
    }

    @Test
    void contains() {
    }

    @Test
    void testContains() {
    }

    @Test
    void removeDuplicateByContains() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void removePreAndLowerFirst() {
    }

    @Test
    void checkStr() {
    }

    @Test
    void removeEnd() {
    }

    @Test
    void removeStart() {
    }

    @Test
    void removeStartByRegex() {
    }

    @Test
    void isAllNotEmpty() {
    }

    @Test
    void containsAny() {
    }

    @Test
    void replaceV3() {
    }

    @Test
    void replaceV4() {
    }

    @Test
    void replace() {
    }

    @Test
    void cast() {
    }

    @Test
    void padLeft() {
    }

    @Test
    void testPadLeft() {
    }

    @Test
    void paddingBySemiColon() {

        String text= """
                ----------------------------------------------------------
                	Application:	[MqTestApp] running! 
                	Local: 	http://localhost:9999/
                	External: 	http://192.168.100.201:9999/
                	Doc: 	http://192.168.100.201:9999/doc.html
                	Env: 	local
                ----------------------------------------------------------
                """;

        System.out.println(QyStr.paddingBySemiColon(text));
    }

    @Test
    void testIsNull() {
    }

    @Test
    void testNotNull() {
    }

    @Test
    void testFilterEmoji1() {
    }

    @Test
    void testFilterEmoji2() {
    }

    @Test
    void testAddStart() {
    }

    @Test
    void testHideName() {
    }

    @Test
    void testHideIdcard() {
    }

    @Test
    void testHideMobile() {
    }

    @Test
    void testContains1() {
    }

    @Test
    void testContains2() {
    }

    @Test
    void containsAtLeast() {
        System.out.println(QyStr.containsAtLeast("abcd", List.of("a"),2));
    }

    @Test
    void testRemoveDuplicateByContains() {
    }

    @Test
    void testRemoveAll() {
    }

    @Test
    void testRemovePreAndLowerFirst() {
    }

    @Test
    void testCheckStr() {
    }

    @Test
    void testRemoveEnd() {
    }

    @Test
    void testRemoveStart() {
    }

    @Test
    void testRemoveStartByRegex() {
    }

    @Test
    void testIsAllNotEmpty() {
    }

    @Test
    void testContainsAny() {
    }

    @Test
    void testReplaceV3() {
    }

    @Test
    void testReplaceV4() {
    }

    @Test
    void testReplace() {
    }

    @Test
    void testCast() {
    }

    @Test
    void testPadLeft1() {
    }

    @Test
    void testPadLeft2() {
    }

    @Test
    void testPaddingBySemiColon() {
    }

    @Test
    void testPaddingBySemiColon1() {
    }
}