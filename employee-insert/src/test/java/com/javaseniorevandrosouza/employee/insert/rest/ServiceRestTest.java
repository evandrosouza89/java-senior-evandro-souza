package com.javaseniorevandrosouza.employee.insert.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaseniorevandrosouza.employee.common.exception.CustomGlobalExceptionHandler;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.insert.business.InsertServiceBusiness;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(CustomGlobalExceptionHandler.class)

@RunWith(SpringRunner.class)
@WebMvcTest(InsertServiceRest.class)
public class ServiceRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InsertServiceBusiness business;

    private TestSpec testSpec;

    private InsertRestResponse response;

    private InsertRestRequest request;

    private ResultActions resultActions;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void post_insert_shall_return_valid_rest_response() throws Exception {
        testSpec.givenAValidRestRequest()
                .givenAValidBusinessResponse()
                .whenRestPost()
                .thenAValidResponseIsReturned();
    }

    @Test
    public void post_insert_invalid_request_shall_return_error() throws Exception {
        testSpec.givenAnInvalidRestRequest()
                .whenRestPost()
                .thenAValidErrorResponseIsReturned();
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
            request = InsertRestRequest.builder()
                    .age(1)
                    .cpf("08895987896")
                    .departmentId(2)
                    .email("user@email.com")
                    .name("name")
                    .phone("25699874552")
                    .build();
            return this;
        }

        TestSpec givenAnInvalidRestRequest() {
            request = InsertRestRequest.builder()
                    .age(-1)
                    .cpf("cpf")
                    .departmentId(-2)
                    .email("email")
                    .phone("phone")
                    .departmentId(-1)
                    .build();
            return this;
        }

        TestSpec givenAValidBusinessResponse() throws ServiceException {
            response = InsertRestResponse.builder()
                    .uuid("876e9101-c3be-49d3-a2f9-121d244597a3")
                    .build();

            when(business.insertEmployee(any(InsertRestRequest.class))).thenReturn(response);
            return this;
        }

        TestSpec whenRestPost() throws Exception {
            resultActions = mvc.perform(post("/colaboradores").characterEncoding("utf-8").content(asJsonString(request))
                    .contentType(APPLICATION_JSON));
            return this;
        }

        TestSpec thenAValidResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.uuid", is("876e9101-c3be-49d3-a2f9-121d244597a3")));
            return this;
        }

        TestSpec thenAValidErrorResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.erros[0]", is("Código: 1000 - Descrição: O preenchimento do campo nome é obrigatório.")))
                    .andExpect(jsonPath("$.erros[1]", is("Código: 1011 - Descrição: O valor do campo email é inválido. Utilize o formato: user@provider.domain.")))
                    .andExpect(jsonPath("$.erros[2]", is("Código: 1012 - Descrição: O valor do campo CPF é inválido. Utilize 11 dígitos podendo ou não conter pontos e traço.")))
                    .andExpect(jsonPath("$.erros[3]", is("Código: 1013 - Descrição: O valor do campo telefone é inválido.")))
                    .andExpect(jsonPath("$.erros[4]", is("Código: 1014 - Descrição: O valor do campo id de departamento é inválido. Utilize valores maiores que zero.")))
                    .andExpect(jsonPath("$.erros[5]", is("Código: 1015 - Descrição: O valor do campo idade é inválido. Utilize valores maiores ou iguais a zero.")));
            return this;
        }
    }
}