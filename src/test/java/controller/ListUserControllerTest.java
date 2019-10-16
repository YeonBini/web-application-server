package controller;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.*;

public class ListUserControllerTest {

    @Test
    public void urlDecoding() throws UnsupportedEncodingException {
        // given
        String encodedValue = "yeonbn88%40gmail.com";

        // then
        assertEquals("yeonbn88@gmail.com", URLDecoder.decode(encodedValue, "utf-8"));

    }

}