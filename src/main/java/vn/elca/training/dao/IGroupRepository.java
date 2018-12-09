package vn.elca.training.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.elca.training.entities.Group;

public interface IGroupRepository extends JpaRepository<Group, Long>, QuerydslPredicateExecutor<Group> {

}
