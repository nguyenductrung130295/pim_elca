package vn.elca.training.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.elca.training.entities.Project;
import vn.elca.training.utils.ProjectStatusEnum;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {
    int countByProjectNumber(int projectNumber);

    void deleteByIdAndStatus(Long id, ProjectStatusEnum status);

    List<Project> findByStatus(ProjectStatusEnum projectStatusByCode);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str%")
    List<Project> findByQuery(@Param("str") String queryStr);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% or p.projectNumber = :num")
    List<Project> findByQuery(@Param("str") String queryStr, @Param("num") int number_project);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% and status = :sta")
    List<Project> findByQuery(@Param("str") String queryStr, @Param("sta") ProjectStatusEnum projectStatusByCode);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% or p.projectNumber = :num and status = :sta")
    List<Project> findByQuery(@Param("str") String queryStr, @Param("sta") ProjectStatusEnum projectStatusByCode,
            @Param("num") int number_project);
}
