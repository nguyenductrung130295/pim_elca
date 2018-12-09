package vn.elca.training.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.elca.training.entities.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {

}