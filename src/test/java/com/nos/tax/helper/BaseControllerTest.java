package com.nos.tax.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper objectMapper;

    public BaseControllerTest() {
        this.objectMapper = new ObjectMapper();
    }

    public String writeValueAsString(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
