package com.paypal.bfs.test.employeeserv.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeResourceImplTest {
	
	@Mock
	private EmployeeService employeeService;
	
	@InjectMocks
	private EmployeeResourceImpl resource;

	@Test
	public void employeeGetByIdTest() throws EmployeeNotFoundException {
		Employee employee = new Employee();
		employee.setId(1);
		when(employeeService.fetchEmployee(eq(1))).thenReturn(employee);
		ResponseEntity<Employee> responseEntity = resource.employeeGetById("1");
		assertThat(responseEntity.getStatusCodeValue(), is(200));
		assertThat(responseEntity.getBody().getId(), is(1));
	}
	
	@Test(expected = EmployeeNotFoundException.class)
	public void employeeGetByIdFailureTest() throws EmployeeNotFoundException {
		Employee employee = new Employee();
		employee.setId(1);
		when(employeeService.fetchEmployee(eq(1))).thenThrow(new EmployeeNotFoundException("Employee not found"));
		resource.employeeGetById("1");
	}
	
	@Test(expected = BadRequestException.class)
	public void insertEmployeeInvalidDateTest() throws BadRequestException {
		Employee employee = new Employee();
		employee.setDateOfBirth("test");
		resource.insertEmployee("uuid", employee);
	}
	
	@Test
	public void insertEmployeeTest() throws BadRequestException {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setDateOfBirth("12-01-2020");
		when(employeeService.insertEmployee(any(), any())).thenReturn(employee);
		ResponseEntity<Employee> responseEntity = resource.insertEmployee("uuid", employee);
		assertThat(responseEntity.getStatusCodeValue(), is(201));
		assertThat(responseEntity.getBody().getId(), is(1));
	}

}
