package com.paypal.bfs.test.employeeserv.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.hamcrest.CoreMatchers.is;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.util.Constants;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {
	
	@Mock
	private EmployeeRepository repository;
	
	@InjectMocks
	private EmployeeService service;

	@Test
	public void fetchEmployeeTest() throws EmployeeNotFoundException {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setDateOfBirth(LocalDate.parse("20-02-2020", Constants.DATE_TIME_FORMATTER));
		entity.setId(1);
		when(repository.findById(any())).thenReturn(Optional.of(entity));
		Employee employee = service.fetchEmployee(1);
		assertThat(employee.getId(), is(1));
	}
	
	@Test(expected = EmployeeNotFoundException.class)
	public void fetchEmployeeFailureTest() throws EmployeeNotFoundException {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setDateOfBirth(LocalDate.parse("20-02-2020", Constants.DATE_TIME_FORMATTER));
		entity.setId(1);
		when(repository.findById(any())).thenReturn(Optional.empty());
		service.fetchEmployee(1);
	}
	
	@Test
	public void insertEmployeeUuidFoundTest() {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setDateOfBirth(LocalDate.parse("20-02-2020", Constants.DATE_TIME_FORMATTER));
		entity.setId(1);
		when(repository.findByUuid(any())).thenReturn(Optional.of(entity));
		Employee employee = new Employee();
		employee.setDateOfBirth("20-02-2020");
		service.insertEmployee("uuid", employee);
		verify(repository, never()).save(any());
	}
	
	@Test
	public void insertEmployeeUuidNotFoundTest() {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setDateOfBirth(LocalDate.parse("20-02-2020", Constants.DATE_TIME_FORMATTER));
		entity.setId(1);
		when(repository.findByUuid(any())).thenReturn(Optional.empty());
		Employee employee = new Employee();
		employee.setDateOfBirth("20-02-2020");
		employee.setAddress(new Address());
		when(repository.save(any())).thenReturn(entity);
		service.insertEmployee("uuid", employee);
		verify(repository, times(1)).save(any());
	}

}
