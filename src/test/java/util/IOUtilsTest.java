package util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void intToBooleanTest () {
        Map<String, String> a1 = new HashMap<>();
        Map<String, String> a2 = new HashMap<>();
        a1.put("a", "1");
        a1.put("b", "2");
        a2.put("a", "1");
        a2.put("b", "2");
        assertEquals(a1, a2);

    }
}
