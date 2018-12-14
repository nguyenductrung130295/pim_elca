package vn.elca.training.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Project> findByStatus(ProjectStatusEnum projectStatusByCode, Pageable pageble);

    int findVersionById(Long id);

    @Query("select p from Project p")
    Page<Project> findAllPaging(Pageable pageble);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str%")
    Page<Project> findByQuery(@Param("str") String queryStr, Pageable pageable);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% or p.projectNumber = :num")
    Page<Project> findByQuery(@Param("str") String queryStr, @Param("num") int number_project, Pageable pageble);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% and status = :sta")
    Page<Project> findByQuery(@Param("str") String queryStr, @Param("sta") ProjectStatusEnum projectStatusByCode,
            Pageable pageble);

    @Query("select p from Project p where upper(p.customer) like %:str% or upper(p.name) like %:str% or p.projectNumber = :num and status = :sta")
    Page<Project> findByQuery(@Param("str") String queryStr, @Param("sta") ProjectStatusEnum projectStatusByCode,
            @Param("num") int number_project, Pageable pageble);
}
