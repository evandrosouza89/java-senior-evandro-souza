package com.javaseniorevandrosouza.employee.search.rest;

import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.search.business.SearchServiceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class SearchServiceRest {

    private final SearchServiceBusiness business;

    @Autowired
    public SearchServiceRest(SearchServiceBusiness business) {
        this.business = business;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new RestRequestValidator());
    }

    @GetMapping(value = "/colaboradores/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchRestResponse search(@Valid SearchRestRequest restRequest, BindingResult bindingResult) throws ServiceException {

        if (bindingResult.hasErrors()) {
            throw ServiceException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .exceptionList(Utils.buildErrorListFromBindingResult(bindingResult))
                    .build();
        }

        log.info("[Search][Rest][Input] GET /colaboradores/{uuid}: " + restRequest.toString());

        SearchRestResponse restResponse = business.searchEmployee(restRequest);

        log.info("[Search][Rest][Output] GET /colaboradores{uuid}: " + restResponse.toString());

        return restResponse;
    }

}