package com.javaseniorevandrosouza.employee.list.department.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaseniorevandrosouza.employee.common.exception.CustomGlobalExceptionHandler;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.list.department.business.ListDepartmentServiceBusiness;
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

import java.util.Arrays;

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
@WebMvcTest(ListDepartmentServiceRest.class)
public class ServiceRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ListDepartmentServiceBusiness business;

    private TestSpec testSpec;

    private EmployeeListDepartmentResponse response;

    private EmployeeListDepartmentRestRequest request;

    private ResultActions resultActions;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void get_list_shall_return_valid_rest_response() throws Exception {
        testSpec.givenAValidRestRequest()
                .givenAValidBusinessResponse()
                .whenRestGet()
                .thenAValidResponseIsReturned();
    }

    @Test
    public void get_list_invalid_request_shall_return_error() throws Exception {
        testSpec.givenAnInvalidRestRequest()
                .whenRestGet()
                .thenAInvalidDepartmentIdErrorResponseIsReturned();
    }

    @Test
    public void get_list_empty_request_shall_return_error() throws Exception {
        testSpec.givenAnEmptyRestRequest()
                .whenRestGet()
                .thenAMissingDepartmentIdErrorResponseIsReturned();
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
            request = EmployeeListDepartmentRestRequest.builder()
                    .departmentId(1)
                    .build();
            return this;
        }

        TestSpec givenAnInvalidRestRequest() {
            request = EmployeeListDepartmentRestRequest.builder()
                    .departmentId(-1)
                    .build();
            return this;
        }

        TestSpec givenAnEmptyRestRequest() {
            request = EmployeeListDepartmentRestRequest.builder()
                    .build();
            return this;
        }

        TestSpec givenAValidBusinessResponse() throws ServiceException {
            EmployeeRestResponse employeeRestResponse1 = EmployeeRestResponse.builder()
                    .name("name1")
                    .email("email1")
                    .build();
            EmployeeRestResponse employeeRestResponse2 = EmployeeRestResponse.builder()
                    .name("name2")
                    .email("email2")
                    .build();

            response = EmployeeListDepartmentResponse.builder()
                    .employeeList(Arrays.asList(employeeRestResponse1, employeeRestResponse2))
                    .build();

            when(business.list(any(EmployeeListDepartmentRestRequest.class))).thenReturn(response);
            return this;
        }

        TestSpec whenRestGet() throws Exception {
            resultActions = mvc.perform(get("/departamentos/" + request.getDepartmentId() + "/colaboradores").characterEncoding("utf-8").content(asJsonString(request))
                    .contentType(APPLICATION_JSON));
            return this;
        }

        TestSpec thenAValidResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.listaDeColaboradores[0].email", is("email1")))
                    .andExpect(jsonPath("$.listaDeColaboradores[0].nome", is("name1")))
                    .andExpect(jsonPath("$.listaDeColaboradores[1].email", is("email2")))
                    .andExpect(jsonPath("$.listaDeColaboradores[1].nome", is("name2")));
            return this;
        }

        TestSpec thenAInvalidDepartmentIdErrorResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.erros[0]", is("Código: 1014 - Descrição: O valor do campo id de departamento é inválido. Utilize valores maiores que zero.")));
            return this;
        }

        TestSpec thenAMissingDepartmentIdErrorResponseIsReturned() throws Exception {
            resultActions.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.erros[1]", is("Código: 1004 - Descrição: O preenchimento do campo departamento é obrigatório. Utilize valores maiores que zero.")));
            return this;
        }
    }
}