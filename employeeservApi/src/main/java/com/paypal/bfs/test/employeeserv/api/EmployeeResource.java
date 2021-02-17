package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @GetMapping(value = "/v1/bfs/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id) throws EmployeeNotFoundException;

    // ----------------------------------------------------------
    // TODO - add a new operation for creating employee resource.
    // ----------------------------------------------------------
    
	/**
     * Inserts employee record.
     * 
     * @Valid annotation helps to validate 
     * the javax validations on the employee POJO
     *
     * @param uuid the uuid
     * @param employee the employee
     * @return the response entity
     * @throws BadRequestException the bad request exception
     */
    @PostMapping(value = "/v1/bfs/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> insertEmployee(
    		@RequestHeader String uuid,
    		@Valid @RequestBody Employee employee) throws BadRequestException;
}
