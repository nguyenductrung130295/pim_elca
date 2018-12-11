package vn.elca.training.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.elca.training.entities.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {
    int countByVisa(String string);
    @Query("select e from Employee e where visa in :visas")
	Set<Employee> findByVisaList(@Param("visas") String[] visas);
}
