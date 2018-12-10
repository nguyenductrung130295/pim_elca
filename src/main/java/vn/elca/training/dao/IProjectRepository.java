package vn.elca.training.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import vn.elca.training.entities.Project;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {
}
