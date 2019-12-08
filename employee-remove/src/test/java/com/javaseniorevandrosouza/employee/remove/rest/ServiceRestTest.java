package com.javaseniorevandrosouza.employee.remove.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaseniorevandrosouza.employee.common.exception.CustomGlobalExceptionHandler;
import com.javaseniorevandrosouza.employee.remove.business.RemoveServiceBusiness;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(CustomGlobalExceptionHandler.class)

@RunWith(SpringRunner.class)
@WebMvcTest(RemoveServiceRest.class)
public class ServiceRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RemoveServiceBusiness business;

    private TestSpec testSpec;

    private String request;

    private ResultActions resultActions;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void delete_remove_shall_a_valid_response() throws Exception {
        testSpec.givenAValidRestRequest()
                .whenRestDelete()
                .thenAValidResponseIsReturned();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class TestSpec {

        TestSpec givenAValidRestRequest() {
            request = "48e6f83f-dac6-443e-8e66-fc67708a8259";
            return this;
        }

        TestSpec whenRestDelete() throws Exception {
            resultActions = mvc.perform(delete("/colaboradores/" + request).characterEncoding("utf-8").content(asJsonString(request))
                    .contentType(APPLICATION_JSON));
            return this;
        }

        TestSpec thenAValidResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is2xxSuccessful());
            return this;
        }
    }
}