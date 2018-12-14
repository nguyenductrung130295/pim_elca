package vn.elca.training.services;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.DifferenceStatusProjectDeleteException;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;

public interface IProjectService {
    Project findProjectById(Long id);

    boolean createProject(Project project, String[] visas);

    void deleteProjectByIdAndNewStatus(Long id) throws ObjectOptimisticLockingFailureException, NullPointerException,
            DifferenceStatusProjectDeleteException;

    void checkProjectNumberExits(int i) throws ProjectNumberAlreadyExistsException;

    void initData();

    void deleteProjectByListIdAndNewStatus(Long[] projectIds) throws NoSuchElementException,
            ObjectOptimisticLockingFailureException, DifferenceStatusProjectDeleteException;

    String getListMemberVisaOfProject(Project project);

    boolean updateProject(Project project, String[] splitVisaMember);

    Page<Project> findProjectByQuery(String queryStr, String queryStatus, int pageNum, int rowOnPage, String sortBy,
            String sortType);

    Page<Project> findProjectAll(int pageNum, int rowOnPage, String sortBy, String sortType);
}
