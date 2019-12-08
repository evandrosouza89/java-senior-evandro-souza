package com.javaseniorevandrosouza.employee.insert.rest;

import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.insert.business.InsertServiceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class InsertServiceRest {

    private final InsertServiceBusiness business;

    @Autowired
    public InsertServiceRest(InsertServiceBusiness business) {
        this.business = business;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new RestRequestValidator());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/colaboradores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public InsertRestResponse insert(@RequestBody @Valid InsertRestRequest insertRestRequest) throws ServiceException {
        log.info("[Insert][Rest][Input] POST /colaboradores: " + insertRestRequest.toString());

        InsertRestResponse restResponse = business.insertEmployee(insertRestRequest);

        log.info("[Insert][Rest][Output] POST /colaboradores: " + restResponse.toString());

        return restResponse;
    }
}