package com.paypal.bfs.test.employeeserv.impl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;
import com.paypal.bfs.test.employeeserv.util.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class for employee resource.
 */
@RestController
@Slf4j
public class EmployeeResourceImpl implements EmployeeResource {
	
	/** The employee service. */
	private EmployeeService employeeService;

	/**
	 * Instantiates a new employee resource impl.
	 *
	 * @param employeeService the employee service
	 */
	public EmployeeResourceImpl(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Fetches employee by id
	 *
	 * @param id the id assigned to employee
	 */
    @Override
    public ResponseEntity<Employee> employeeGetById(String id) throws EmployeeNotFoundException {

    	log.info("Fetching employee for id -> {}", id);
    	
        Employee employee = employeeService.fetchEmployee(Integer.valueOf(id));

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
	 * Inserts an employee details into DB
	 *
	 * @param uuid the unique id headers passed for each request which helps to make this request idempotent
	 * @param employee the employee details to be persisted
	 */
	@Override
	public ResponseEntity<Employee> insertEmployee(String uuid, Employee employee) throws BadRequestException {
		
		log.info("Incoming uuid -> {} and employee insert request -> {}", uuid, employee);
		
		validateDate(employee.getDateOfBirth());
		
		Employee persistedEmployee = employeeService.insertEmployee(uuid, employee);
		
		return new ResponseEntity<>(persistedEmployee, HttpStatus.CREATED);
	}
	
	/**
	 * Validates date as per the required format.
	 *
	 * @param date the date
	 * @throws BadRequestException the bad request exception
	 */
	private void validateDate(String date) throws BadRequestException {
		try {
			LocalDate.parse(date, Constants.DATE_TIME_FORMATTER);
		} catch(DateTimeParseException exception) {
			throw new BadRequestException("dateOfBirth must be of format dd-MM-yyyy");
		}
	}
}
