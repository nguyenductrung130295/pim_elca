package vn.elca.training.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.elca.training.entities.Project;

public interface IProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {

}
