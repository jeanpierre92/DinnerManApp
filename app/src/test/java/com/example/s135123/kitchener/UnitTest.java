package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.s135123.kitchener.SendRequest;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Override;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTest {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Test
    public void inputStreamToStringTest() throws Exception {
        String testString="test";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        assertEquals(testString, new SendRequest().convertInputStreamToString(in));
    }
}