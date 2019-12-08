package com.javaseniorevandrosouza.employee.remove.rest;

import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.remove.business.RemoveServiceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RemoveServiceRest {

    private final RemoveServiceBusiness business;

    @Autowired
    public RemoveServiceRest(RemoveServiceBusiness business) {
        this.business = business;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/colaboradores/{uuid}")
    public void remove(@PathVariable String uuid) throws ServiceException {
        log.info("[Remove][Rest][Input] DELETE /colaboradores: " + uuid);

        business.removeEmployee(uuid);

        log.info("[Remove][Rest][Output] DELETE /colaboradores: " + uuid + " removed");

    }

}