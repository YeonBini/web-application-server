package controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestMappingTest {

    @Test
    public void getController() {
        // given
        String url = "/user/create";

        // then
        assertNotNull(RequestMapping.getController(url));
    }

}