package com.javaseniorevandrosouza.employee.search.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaseniorevandrosouza.employee.common.exception.CustomGlobalExceptionHandler;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.search.business.SearchServiceBusiness;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(CustomGlobalExceptionHandler.class)

@RunWith(SpringRunner.class)
@WebMvcTest(SearchServiceRest.class)
public class ServiceRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SearchServiceBusiness business;

    private TestSpec testSpec;

    private SearchRestResponse response;

    private SearchRestRequest request;

    private ResultActions resultActions;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void get_search_valid_request_shall_return_valid_rest_response() throws Exception {
        testSpec.givenAValidRestRequest()
                .givenAValidBusinessResponse()
                .whenRestGet()
                .thenAValidResponseIsReturned();
    }

    @Test
    public void get_search_invalid_request_shall_return_error_response() throws Exception {
        testSpec.givenAnInvalidRestRequest()
                .whenRestGet()
                .thenUuidInvalidErrorResponseIsReturned();
    }

    @Test
    public void get_search_empty_request_shall_return_error_response() throws Exception {
        testSpec.givenAnEmptyRestRequest()
                .whenRestGet()
                .thenUuidMissingErrorResponseIsReturned();
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
            request = SearchRestRequest.builder()
                    .uuid("2544abcf-13e1-4238-ac0a-5fcd5f9a4710")
                    .build();
            return this;
        }

        TestSpec givenAnInvalidRestRequest() {
            request = SearchRestRequest.builder()
                    .uuid("2544abcf-13e1-4238-ac0a")
                    .build();
            return this;
        }

        TestSpec givenAnEmptyRestRequest() {
            request = SearchRestRequest.builder()
                    .build();
            return this;
        }

        TestSpec givenAValidBusinessResponse() throws ServiceException {
            response = SearchRestResponse.builder()
                    .age(55)
                    .cpf("02215689775")
                    .departmentId(2)
                    .email("email@email.com")
                    .name("name")
                    .phone("2254689")
                    .build();

            when(business.searchEmployee(any(SearchRestRequest.class))).thenReturn(response);
            return this;
        }

        TestSpec whenRestGet() throws Exception {
            resultActions = mvc.perform(get("/colaboradores/" + request.getUuid()).characterEncoding("utf-8").content(asJsonString(request))
                    .contentType(APPLICATION_JSON));
            return this;
        }

        TestSpec thenAValidResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.cpf", is(response.getCpf())))
                    .andExpect(jsonPath("$.email", is(response.getEmail())))
                    .andExpect(jsonPath("$.nome", is(response.getName())))
                    .andExpect(jsonPath("$.telefone", is(response.getPhone())))
                    .andExpect(jsonPath("$.departamento", is(response.getDepartmentId())))
                    .andExpect(jsonPath("$.idade", is(response.getAge())));
            return this;
        }

        TestSpec thenUuidInvalidErrorResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.erros[0]", is("Código: 1016 - Descrição: O valor do campo uuid é inválido. Utilize 36 caracteres incluindo traços.")));
            return this;
        }

        TestSpec thenUuidMissingErrorResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.erros[0]", is("Código: 1006 - Descrição: O preenchimento do campo uuid é obrigatório. Utilize 36 caracteres incluindo traços.")));
            return this;
        }
    }
}