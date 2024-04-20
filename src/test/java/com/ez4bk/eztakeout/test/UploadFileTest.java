package com.ez4bk.eztakeout.test;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UploadFileTest {
    @Test
    public void testUpload() {
        String fileName = "test.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String uuidFileName = UUID.randomUUID() + suffix;
        System.out.println(uuidFileName);
    }
}

