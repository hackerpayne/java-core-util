package com.qyhstech.core.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DirUtilTest {
    @Test
    public void testIsDirectory() throws Exception {
    }

    @Test
    public void testIsDirectory1() throws Exception {
    }

    @Test
    public void testCreateDir() throws Exception {
    }

    @Test
    public void testDelDir() throws Exception {
    }

    @Test
    public void testIsDirEmpty() throws Exception {
    }

    @Test
    public void testIsDirEmpty1() throws Exception {
    }

    @Test
    public void testGetDirDirectory() throws Exception {
        List<File> list = QyDir.getDirDirectory("/data");
        System.out.println(Arrays.toString(list.toArray()));

    }

}