package com.paypal.bfs.test.employeeserv.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.util.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class EmployeeService.
 */
@Service
@Slf4j
public class EmployeeService {

	/** The employee repository. */
	private EmployeeRepository employeeRepository;

	/**
	 * Instantiates a new employee service.
	 *
	 * @param employeeRepository the employee repository
	 */
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	/**
	 * Fetch employee.
	 *
	 * @param id the id
	 * @return the employee
	 * @throws EmployeeNotFoundException the employee not found exception
	 */
	public Employee fetchEmployee(Integer id) throws EmployeeNotFoundException {

		Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);

		if (!employeeEntity.isPresent()) {
			log.warn("Employee details not found for requested id -> {}", id);
			throw new EmployeeNotFoundException("Employee information not found in DB");
		}
		
		log.info("Fetched the employee details successfully for id -> {}", id);
		
		Employee employee = convertToEmployee(employeeEntity.get());
		
		return employee;

	}
	
	/**
	 * Insert employee.
	 * 
	 * Looks for an employee already present for incoming uuid, if not proceeds with inserting
	 *
	 * @param uuid the uuid
	 * @param employee the employee
	 * @return the employee
	 */
	public Employee insertEmployee(String uuid, Employee employee) {
		
		Employee persistedEmployee = new Employee();
		
		Optional<EmployeeEntity> employeeByUuid = employeeRepository.findByUuid(uuid);
		
		if (employeeByUuid.isPresent()) {
			log.warn("Employee is already inserted for the requested uuid -> {}", uuid);
			persistedEmployee.setId(employeeByUuid.get().getId());
			return persistedEmployee;
		}
		
		EmployeeEntity employeeEntity = convertToEmployeeEntity(employee);
		employeeEntity.setUuid(uuid);
		
		EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
		
		log.info("Persisted the employee details in DB and assigned the id -> {}", savedEntity.getId());
		persistedEmployee.setId(savedEntity.getId());
		
		return persistedEmployee;
	}

	/**
	 * Convert employee entity to employee.
	 *
	 * @param employeeEntity the employee entity
	 * @return the employee
	 */
	private Employee convertToEmployee(EmployeeEntity employeeEntity) {

		Employee employee = new Employee();
		employee.setId(employeeEntity.getId());
		employee.setFirstName(employeeEntity.getFirstName());
		employee.setLastName(employeeEntity.getLastName());
		employee.setDateOfBirth(Constants.DATE_TIME_FORMATTER.format(employeeEntity.getDateOfBirth()));
		Address address = new Address();
		address.setLine1(employeeEntity.getAddressLine1());
		address.setLine2(employeeEntity.getAddressLine2());
		address.setCity(employeeEntity.getCity());
		address.setState(employeeEntity.getState());
		address.setCountry(employeeEntity.getCountry());
		address.setZipCode(employeeEntity.getZipCode());
		employee.setAddress(address);
		
		return employee;
	}
	
	/**
	 * Convert employee to employee entity.
	 *
	 * @param employee the employee
	 * @return the employee entity
	 */
	private EmployeeEntity convertToEmployeeEntity(Employee employee) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setFirstName(employee.getFirstName());
		employeeEntity.setLastName(employee.getLastName());
		employeeEntity.setDateOfBirth(LocalDate.parse(employee.getDateOfBirth(), Constants.DATE_TIME_FORMATTER));
		employeeEntity.setAddressLine1(employee.getAddress().getLine1());
		employeeEntity.setAddressLine2(employee.getAddress().getLine2());
		employeeEntity.setCity(employee.getAddress().getCity());
		employeeEntity.setState(employee.getAddress().getState());
		employeeEntity.setCountry(employee.getAddress().getCountry());
		employeeEntity.setZipCode(employee.getAddress().getZipCode());
		
		return employeeEntity;
	}
}
