package com.paypal.bfs.test.employeeserv.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;

/**
 * The Interface EmployeeRepository.
 */
@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Integer> {
	
	/**
	 * Find by uuid.
	 *
	 * @param uuid the uuid
	 * @return the optional employee entity
	 */
	Optional<EmployeeEntity> findByUuid(String uuid);
}
